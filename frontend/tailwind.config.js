/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    /*colors: {
      yellow: '#FFCE15',
      red: '##FA0A11',
    },*/
    extend: {
      colors: {
        yellow: '#FFCE15',
        red: '#FA0A11',
      },
    },
  },
  plugins: [],
}

