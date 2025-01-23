import {ReactElement, useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

interface IWitChildren {
    children: ReactElement | ReactElement[]
}

export function Background({children}: IWitChildren) {
    const {theme} = useContext<ISettingsContext>(SettingsContext)

    switch (theme) {
        case "Winter":
            return (
                <div className={"h-screen w-screen overflow-x-hidden bg-cover bg-center"}
                     style={{backgroundImage: "url('https://storage.googleapis.com/image_bucket_ip2/general/winter-achtergrond.gif')"}}>
                    {children}
                </div>

            )
        default:
            return (
                <div className={"h-screen w-screen overflow-x-hidden bg-cover bg-center"}
                     style={{backgroundImage: "url('https://storage.googleapis.com/image_bucket_ip2/general/achtergrond.png')"}}>
                    {children}
                </div>


            )
    }
}