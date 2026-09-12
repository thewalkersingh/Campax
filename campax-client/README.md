# Campax Admin Dashboard - from-scratch setup

## 1. Scaffold a fresh Vite + React app

```bash
npm create vite@latest campax-dashboard -- --template react
cd campax-dashboard
npm install
npm install tailwindcss @tailwindcss/vite
```

## 2. Overlay these files

Copy the files from this zip into the project the CLI just created,
overwriting where they already exist:

```
vite.config.js          -> overwrite (adds the Tailwind plugin)
src/index.css           -> overwrite (Tailwind v4's single-line import)
src/App.jsx             -> overwrite (renders the dashboard)
src/dashboard/          -> new folder, copy in full
```

You can delete `src/App.css` and anything under `src/assets/` from the
default template - they're not used.

## 3. Backend requirements

- Spring Boot app running on `http://localhost:8080` (hardcoded in
  `src/dashboard/lib/apiClient.js` for now).
- Make sure `com.campax.campaxserver.config.CorsConfig` exists in the
  backend (shared in an earlier message) - without it the browser
  blocks every request from `http://localhost:5173` to the API.

## 4. Run it

```bash
npm run dev
```

Open the URL Vite prints (usually `http://localhost:5173`).

## What's in here

Only `School` management - list (search + pagination), create, edit,
and deactivate/reactivate - because that's the only backend feature
built end-to-end so far. The sidebar shows Students/Staff/Settings as
disabled placeholders, ready for when those controllers exist. Nothing
is mocked; every action calls the real `/api/v1/platform/schools` API.

## File map

```
vite.config.js
src/
├── index.css
├── App.jsx
└── dashboard/
    ├── lib/
    │   ├── apiClient.js      - fetch wrapper, parses the backend's ProblemDetail error shape
    │   └── schoolApi.js      - list/create/update/deactivate/reactivate calls
    ├── components/
    │   ├── Sidebar.jsx
    │   ├── Modal.jsx
    │   ├── ConfirmDialog.jsx
    │   ├── StatusBadge.jsx
    │   └── SchoolFormModal.jsx
    ├── pages/
    │   └── SchoolsPage.jsx
    └── AdminDashboard.jsx     - top-level export, renders Sidebar + SchoolsPage
```
