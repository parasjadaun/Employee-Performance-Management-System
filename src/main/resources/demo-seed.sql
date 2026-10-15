INSERT INTO departments (name) VALUES
    ('Engineering'), ('People Operations'), ('Sales')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (name, email, password_hash, role, department_id)
SELECT 'EPMS Admin', 'admin@epms.com',
       '$2a$12$hhKovMC1g5FmVO1Kd5IPmeagasylQuXkZwpk1pJhYKzVjh7GGNavu',
       'ADMIN', id
FROM departments WHERE name = 'People Operations'
ON CONFLICT (email) DO UPDATE SET active = TRUE;

INSERT INTO users (name, email, password_hash, role, department_id)
SELECT 'Maya Manager', 'manager@epms.com',
       '$2a$12$/LXqBH3WwVfK7J9EBrVm0uhsmLwzbQwR5AY7HEw5WjQFqk9YfPkVS',
       'MANAGER', id
FROM departments WHERE name = 'Engineering'
ON CONFLICT (email) DO UPDATE SET active = TRUE;

INSERT INTO users (name, email, password_hash, role, department_id)
SELECT 'Noah Manager', 'manager2@epms.com',
       '$2a$12$/LXqBH3WwVfK7J9EBrVm0uhsmLwzbQwR5AY7HEw5WjQFqk9YfPkVS',
       'MANAGER', id
FROM departments WHERE name = 'Sales'
ON CONFLICT (email) DO UPDATE SET active = TRUE;

INSERT INTO users (name, email, password_hash, role, department_id, manager_id)
SELECT 'Aarav Employee', 'employee@epms.com',
       '$2a$12$hM1bBmeNNy.1ScT.OWC1pOcJ/ILnghVCM2r/IYLRbbhGdprjsWD0O',
       'EMPLOYEE', d.id, m.id
FROM departments d
JOIN users m ON m.email = 'manager@epms.com'
WHERE d.name = 'Engineering'
ON CONFLICT (email) DO UPDATE SET active = TRUE, manager_id = EXCLUDED.manager_id;

INSERT INTO users (name, email, password_hash, role, department_id, manager_id)
SELECT names.name, names.email,
       '$2a$12$hM1bBmeNNy.1ScT.OWC1pOcJ/ILnghVCM2r/IYLRbbhGdprjsWD0O',
       'EMPLOYEE', d.id, m.id
FROM (VALUES
    ('Diya Employee', 'employee2@epms.com'),
    ('Kabir Employee', 'employee3@epms.com'),
    ('Sara Employee', 'employee4@epms.com'),
    ('Ishaan Employee', 'employee5@epms.com')
) AS names(name, email)
CROSS JOIN departments d
JOIN users m ON m.email = 'manager2@epms.com'
WHERE d.name = 'Sales'
ON CONFLICT (email) DO UPDATE SET active = TRUE, manager_id = EXCLUDED.manager_id;

INSERT INTO review_cycles (name, start_date, end_date, status)
VALUES ('Q4 2026', DATE '2026-10-01', DATE '2026-12-31', 'OPEN')
ON CONFLICT (name) DO NOTHING;

INSERT INTO goals (employee_id, manager_id, title, description, weight, due_date, progress)
SELECT e.id, m.id, 'Complete EPMS release', 'Deliver the first production-ready release.', 60, DATE '2026-12-15', 70
FROM users e CROSS JOIN users m
WHERE e.email = 'employee@epms.com' AND m.email = 'manager@epms.com'
  AND NOT EXISTS (
      SELECT 1 FROM goals g WHERE g.employee_id = e.id AND g.title = 'Complete EPMS release'
  );

INSERT INTO goals (employee_id, manager_id, title, description, weight, due_date, progress)
SELECT e.id, m.id, 'Improve automated test coverage', 'Add focused unit and integration tests.', 40, DATE '2026-12-20', 45
FROM users e CROSS JOIN users m
WHERE e.email = 'employee@epms.com' AND m.email = 'manager@epms.com'
  AND NOT EXISTS (
      SELECT 1 FROM goals g WHERE g.employee_id = e.id AND g.title = 'Improve automated test coverage'
  );

INSERT INTO reviews (cycle_id, employee_id, reviewer_id, rating, comments, strengths, improvements, type)
SELECT c.id, e.id, m.id, 4, 'Strong delivery and collaboration.', 'Ownership and communication.',
       'Continue improving test automation.', 'MANAGER'
FROM review_cycles c CROSS JOIN users e CROSS JOIN users m
WHERE c.name = 'Q4 2026' AND e.email = 'employee@epms.com' AND m.email = 'manager@epms.com'
  AND NOT EXISTS (
      SELECT 1 FROM reviews r WHERE r.cycle_id = c.id AND r.employee_id = e.id
        AND r.reviewer_id = m.id AND r.type = 'MANAGER'
  );

INSERT INTO notifications (user_id, message)
SELECT id, 'Welcome to the EPMS demo environment.'
FROM users
WHERE email IN ('admin@epms.com', 'manager@epms.com', 'employee@epms.com')
  AND NOT EXISTS (
      SELECT 1 FROM notifications n
      WHERE n.user_id = users.id AND n.message = 'Welcome to the EPMS demo environment.'
  );
