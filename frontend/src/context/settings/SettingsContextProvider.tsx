import { ReactElement } from 'react'
import SettingsContext from './SettingsContext'
import { useLocalStorage } from 'usehooks-ts'

interface WithChildren {
    children: ReactElement | ReactElement[]
}

export default function SettingsContextProvider({ children }: WithChildren) {
    const [soundLevel, setSoundLevelLS] = useLocalStorage('sound-level', 100)
    const [currency, setCurrencyLS] = useLocalStorage('currency', "Dollar")
    const [language, setLanguageLS] = useLocalStorage('language', "EN")
    const [theme, setThemeLs] = useLocalStorage('theme', "default")

    const setSoundLevel = (newSoundLevel : number) => setSoundLevelLS(newSoundLevel)

    const setCurrency = (newCurrency : string) => setCurrencyLS(newCurrency)

    const setLanguage = (newLanguage : string) => setLanguageLS(newLanguage)

    const setTheme = (newTheme : string) => setThemeLs(newTheme)

    return (
        <SettingsContext.Provider value={{
            soundLevel,
            setSoundLevel,
            currency,
            setCurrency,
            language,
            setLanguage,
            theme,
            setTheme: setTheme
        }}>
            {children}
        </SettingsContext.Provider>
    )
}
