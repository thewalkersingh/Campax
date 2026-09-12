// vite.config.js
// Overwrites the default file the Vite CLI generates - just adds the
// Tailwind v4 plugin alongside the existing React plugin.
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [react(), tailwindcss()],
});
