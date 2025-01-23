import { createContext } from 'react'

export interface ISettingsContext {
    soundLevel: number
    setSoundLevel: (level : number) => void

    currency: string
    setCurrency: (currency : string) => void

    language: string
    setLanguage: (language : string) => void

    theme: string
    setTheme: (theme : string) => void
}

export default createContext<ISettingsContext>({
    soundLevel: 100,
    setSoundLevel: () => {},

    currency: "Dollar",
    setCurrency: () => {},

    language: "EN",
    setLanguage: () => {},

    theme: "default",
    setTheme: () => {}
})
