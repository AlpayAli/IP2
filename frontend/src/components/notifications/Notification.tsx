import {format} from "date-fns";
import {Close} from "@mui/icons-material";

interface INotificationProps {
    date: string,
    notificationText: () => string,
    buttonText: () => string,
    accept: () => void,
    decline: () => void
}

export function Notification({date, notificationText, accept, decline, buttonText}: INotificationProps) {
    return (
        <div
            className={"grid grid-cols-12 rounded-xl shadow-black shadow-lg bg-blue-500 m-1 p-3 opacity-90"}>
            <div
                className={"col-span-2 text-white font-bold text-[10px] flex justify-center items-center"}>{format(new Date(date), 'dd MMM yyyy, hh:mm:ss a')}</div>
            <div className={"col-span-7 "}>
                {notificationText()}
            </div>

            <div className={"col-span-2 "}>
                <button
                    className={"bg-yellow text-blue-500 font-bold p-2 shadow-black shadow-lg rounded-xl hover:opacity-70"}
                    onClick={() => {
                        accept()
                    }}>{buttonText()}</button>
            </div>
            <div
                onClick={() => {
                    decline()
                }}
                className={"hover:cursor-pointer hover:scale-110"}>
                <Close sx={{
                    width: "100%",
                    height: "100%",
                    marginY: "auto",
                    marginX: "auto",
                    color: "red"
                }}>
                </Close>
            </div>

        </div>
    )

}