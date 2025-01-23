import {useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function Loading() {
    const {theme} = useContext<ISettingsContext>(SettingsContext);

    return <div className={"h-screen w-screen bg-gray-900 overflow-hidden"}>
        <img
            className={"object-cover w-full h-full"}
            {...theme === "Winter" ? {src: "https://storage.googleapis.com/image_bucket_ip2/general/winter-loading-screen.gif"} : {src: "https://storage.googleapis.com/image_bucket_ip2/general/loading%20screen.gif"}}
            alt={"loading"}/>
        Loading
    </div>
}