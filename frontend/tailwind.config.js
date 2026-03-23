/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        primary: { DEFAULT: '#1a56db', light: '#e8f0fe', dark: '#1341b0' },
        accent: '#0ea5e9',
        surface: '#f8faff',
        border: '#dbe4f0',
      },
      fontFamily: {
        sans: ['Outfit', 'sans-serif'],
        display: ['Sora', 'sans-serif'],
      },
      boxShadow: {
        card: '0 2px 12px 0 rgba(26,86,219,0.07)',
        modal: '0 8px 40px 0 rgba(26,86,219,0.15)',
      },
    },
  },
  plugins: [],
}
