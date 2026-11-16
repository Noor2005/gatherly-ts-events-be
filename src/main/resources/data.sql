-- Seed data for mentor specialties.
-- Runs on every startup (spring.sql.init.mode=always), so each insert must be idempotent:
-- ON CONFLICT relies on the unique constraint uk_specialties_name.
INSERT INTO specialties (name) VALUES ('Backend Development') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('Frontend Development') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('Mobile Development') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('DevOps & Cloud') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('Data Science & AI') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('Cybersecurity') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('UI/UX Design') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('Product Management') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('QA & Testing') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('Career Change') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('Interview Prep') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('Resume & LinkedIn Review') ON CONFLICT (name) DO NOTHING;
INSERT INTO specialties (name) VALUES ('Leadership & Management') ON CONFLICT (name) DO NOTHING;

-- Seed data for example mentors (Ask a Tech Sister).
-- Order matters: users -> mentor_profiles -> mentor_specialties, because of the foreign keys.
-- User ids are fixed UUIDs because Hibernate normally generates them in Java, not in the DB.
-- Every insert is idempotent so restarts don't create duplicates.

-- 1) Users that the mentor profiles belong to
INSERT INTO techsister_user (id, name, email, country, role, otp, created_date, created_by, modified_date, modified_by)
VALUES ('00000000-0000-0000-0000-000000000001', 'Grace Witter', 'grace.witter@example.com', 'United States', 'USER', 0, NOW(), 'seed', NOW(), 'seed')
ON CONFLICT (email) DO NOTHING;
INSERT INTO techsister_user (id, name, email, country, role, otp, created_date, created_by, modified_date, modified_by)
VALUES ('00000000-0000-0000-0000-000000000002', 'Amara Okafor', 'amara.okafor@example.com', 'Nigeria', 'USER', 0, NOW(), 'seed', NOW(), 'seed')
ON CONFLICT (email) DO NOTHING;
INSERT INTO techsister_user (id, name, email, country, role, otp, created_date, created_by, modified_date, modified_by)
VALUES ('00000000-0000-0000-0000-000000000003', 'Priya Sharma', 'priya.sharma@example.com', 'India', 'USER', 0, NOW(), 'seed', NOW(), 'seed')
ON CONFLICT (email) DO NOTHING;
INSERT INTO techsister_user (id, name, email, country, role, otp, created_date, created_by, modified_date, modified_by)
VALUES ('00000000-0000-0000-0000-000000000004', 'Laila Haddad', 'laila.haddad@example.com', 'Jordan', 'USER', 0, NOW(), 'seed', NOW(), 'seed')
ON CONFLICT (email) DO NOTHING;
INSERT INTO techsister_user (id, name, email, country, role, otp, created_date, created_by, modified_date, modified_by)
VALUES ('00000000-0000-0000-0000-000000000005', 'Sofia Martinez', 'sofia.martinez@example.com', 'Spain', 'USER', 0, NOW(), 'seed', NOW(), 'seed')
ON CONFLICT (email) DO NOTHING;

-- 2) Mentor profiles, looked up by email so they link to the right user
INSERT INTO mentor_profiles (user_id, headline, bio, meeting_link, available)
SELECT id, 'Senior Software Engineer', 'Backend engineer with 10 years of Java and Spring experience. Happy to talk about career switching and system design.', 'https://meet.google.com/abc-defg-hij', true
FROM techsister_user WHERE email = 'grace.witter@example.com'
ON CONFLICT (user_id) DO NOTHING;
INSERT INTO mentor_profiles (user_id, headline, bio, meeting_link, available)
SELECT id, 'Frontend Lead', 'I build accessible React apps and love helping juniors get their first frontend role.', 'https://meet.google.com/klm-nopq-rst', false
FROM techsister_user WHERE email = 'amara.okafor@example.com'
ON CONFLICT (user_id) DO NOTHING;
INSERT INTO mentor_profiles (user_id, headline, bio, meeting_link, available)
SELECT id, 'Data Scientist', 'Working on ML in production. Ask me about moving from analytics into data science and AI.', 'https://meet.google.com/uvw-xyza-bcd', true
FROM techsister_user WHERE email = 'priya.sharma@example.com'
ON CONFLICT (user_id) DO NOTHING;
INSERT INTO mentor_profiles (user_id, headline, bio, meeting_link, available)
SELECT id, 'Cloud & DevOps Engineer', 'AWS certified, running Kubernetes at scale. I can help with DevOps roadmaps and interview prep.', 'https://meet.google.com/efg-hijk-lmn', false
FROM techsister_user WHERE email = 'laila.haddad@example.com'
ON CONFLICT (user_id) DO NOTHING;
INSERT INTO mentor_profiles (user_id, headline, bio, meeting_link, available)
SELECT id, 'Product Manager', 'Former engineer turned PM. Let''s talk product thinking, leadership and polishing your resume.', 'https://meet.google.com/opq-rstu-vwx', false
FROM techsister_user WHERE email = 'sofia.martinez@example.com'
ON CONFLICT (user_id) DO NOTHING;

-- 3) Mentor <-> specialty links, matched by email and specialty name
INSERT INTO mentor_specialties (mentor_id, specialty_id)
SELECT m.mentor_id, s.specialty_id
FROM (VALUES
    ('grace.witter@example.com',   'Backend Development'),
    ('grace.witter@example.com',   'Career Change'),
    ('amara.okafor@example.com',   'Frontend Development'),
    ('amara.okafor@example.com',   'UI/UX Design'),
    ('priya.sharma@example.com',   'Data Science & AI'),
    ('priya.sharma@example.com',   'Career Change'),
    ('laila.haddad@example.com',   'DevOps & Cloud'),
    ('laila.haddad@example.com',   'Interview Prep'),
    ('sofia.martinez@example.com', 'Product Management'),
    ('sofia.martinez@example.com', 'Leadership & Management'),
    ('sofia.martinez@example.com', 'Resume & LinkedIn Review')
) AS seed(email, specialty)
JOIN techsister_user u ON u.email = seed.email
JOIN mentor_profiles m ON m.user_id = u.id
JOIN specialties s ON s.name = seed.specialty
WHERE NOT EXISTS (
    SELECT 1 FROM mentor_specialties ms
    WHERE ms.mentor_id = m.mentor_id AND ms.specialty_id = s.specialty_id
);
