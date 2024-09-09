/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: '#4a90e2',
        secondary: '#50e3c2',
        accent: '#b8e986',
        background: '#1a1a1a',
        foreground: '#ffffff',
      },
    },
  },
  plugins: [],
}