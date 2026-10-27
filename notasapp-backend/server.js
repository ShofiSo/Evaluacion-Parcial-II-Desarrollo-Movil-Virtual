const express = require('express');
const cors = require('cors');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { DatabaseSync } = require('node:sqlite'); // SQLite incluido en Node.js (no requiere compilar nada)

const SECRET = process.env.JWT_SECRET || 'cambia-este-secreto';
const PORT = process.env.PORT || 3000;

const db = new DatabaseSync('notas.db');
db.exec(`
CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  email TEXT UNIQUE NOT NULL,
  password TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS notes (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER NOT NULL,
  title TEXT NOT NULL,
  content TEXT NOT NULL,
  created_at TEXT NOT NULL DEFAULT (datetime('now')),
  FOREIGN KEY (user_id) REFERENCES users(id)
);`);

// Usuario de prueba
if (!db.prepare('SELECT id FROM users WHERE email = ?').get('demo@correo.com')) {
  db.prepare('INSERT INTO users (name, email, password) VALUES (?, ?, ?)')
    .run('Usuario Demo', 'demo@correo.com', bcrypt.hashSync('123456', 10));
}

const app = express();
app.use(cors());
app.use(express.json());

// Middleware: valida el token JWT enviado en "Authorization: Bearer <token>"
function auth(req, res, next) {
  const header = req.headers.authorization || '';
  const token = header.startsWith('Bearer ') ? header.slice(7) : null;
  if (!token) return res.status(401).json({ message: 'Token requerido' });
  try {
    req.user = jwt.verify(token, SECRET);
    next();
  } catch {
    res.status(401).json({ message: 'Token inválido o expirado' });
  }
}

app.post('/api/auth/register', (req, res) => {
  const { name, email, password } = req.body;
  if (!name || !email || !password) return res.status(400).json({ message: 'Datos incompletos' });
  try {
    const info = db.prepare('INSERT INTO users (name, email, password) VALUES (?, ?, ?)')
      .run(name, email, bcrypt.hashSync(password, 10));
    res.status(201).json({ id: info.lastInsertRowid, name, email });
  } catch {
    res.status(409).json({ message: 'El email ya está registrado' });
  }
});

app.post('/api/auth/login', (req, res) => {
  const { email, password } = req.body;
  const user = db.prepare('SELECT * FROM users WHERE email = ?').get(email);
  if (!user || !bcrypt.compareSync(password || '', user.password)) {
    return res.status(401).json({ message: 'Credenciales incorrectas' });
  }
  const token = jwt.sign({ id: user.id, email: user.email }, SECRET, { expiresIn: '8h' });
  res.json({ token, user: { id: user.id, name: user.name, email: user.email } });
});

// Crear nota (asociada al usuario del token)
app.post('/api/notes', auth, (req, res) => {
  const { title, content } = req.body;
  if (!title || !content) return res.status(400).json({ message: 'Título y contenido son obligatorios' });
  const info = db.prepare('INSERT INTO notes (user_id, title, content) VALUES (?, ?, ?)')
    .run(req.user.id, title, content);
  res.status(201).json(db.prepare('SELECT id, title, content, created_at FROM notes WHERE id = ?').get(info.lastInsertRowid));
});

// Listar notas del usuario autenticado
app.get('/api/notes', auth, (req, res) => {
  res.json(db.prepare('SELECT id, title, content, created_at FROM notes WHERE user_id = ? ORDER BY id DESC').all(req.user.id));
});

app.get('/api', (req, res) => res.json({ status: 'ok', message: 'API de NotasApp funcionando OK' }));
app.listen(PORT, '0.0.0.0', () => console.log(`API lista en http://localhost:${PORT}/api`));