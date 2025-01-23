import {
    AttachMoney,
    CurrencyBitcoin,
    CurrencyFranc,
    CurrencyLira, CurrencyPound,
    CurrencyYen,
    CurrencyYuan,
    Euro
} from "@mui/icons-material";

interface CurrencyIconProps {
    currency: string
}

export function CurrencyIcon({currency} : CurrencyIconProps) {
    switch (currency) {
        case "Bitcoin":
            return (
                    <CurrencyBitcoin className={"float-left"} sx={{
                        width: "100%",
                        height: "100%",
                        marginY: "auto",
                        color: "#fcd34d"
                    }}/>
            )
        case "Franc":
            return (
                    <CurrencyFranc className={"float-left"} sx={{
                        width: "100%",
                        height: "100%",
                        marginY: "auto",
                        color: "#fcd34d"
                    }}/>
            )
        case "Lira":
            return (
                    <CurrencyLira className={"float-left"} sx={{
                        width: "100%",
                        height: "100%",
                        marginY: "auto",
                        color: "#fcd34d"
                    }}/>
            )
        case "Pound":
            return (
                    <CurrencyPound className={"float-left"} sx={{
                        width: "100%",
                        height: "100%",
                        marginY: "auto",
                        color: "#fcd34d"
                    }}/>
            )
        case "Yen":
            return (
                    <CurrencyYen className={"float-left"} sx={{
                        width: "100%",
                        height: "100%",
                        marginY: "auto",
                        color: "#fcd34d"
                    }}/>
            )
        case "Yuan":
            return (
                    <CurrencyYuan className={"float-left"} sx={{
                        width: "100%",
                        height: "100%",
                        marginY: "auto",
                        color: "#fcd34d"
                    }}/>
            )
        case "Euro":
            return (
                    <Euro className={"float-left"} sx={{
                        width: "100%",
                        height: "100%",
                        marginY: "auto",
                        color: "#fcd34d"
                    }}/>
            )
        default:
            return (
                    <AttachMoney className={"float-left"} sx={{
                        width: "100%",
                        height: "100%",
                        marginY: "auto",
                        color: "#fcd34d"
                    }}/>
            )
    }

}