import {useChangeName} from "../../hooks/usePlayer.ts";
import {CreateSharp, Star} from "@mui/icons-material";
import {useContext, useState} from "react";
import {Player} from "../../model/Player.ts";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";
import {CurrencyIcon} from "./CurrencyIcon.tsx";

interface AccountProps {
    currentPlayer: Player
}

function Account({currentPlayer}: AccountProps) {
    const {changeName} = useChangeName();
    const [nameValue, setNameValue] = useState(currentPlayer.name);
    const [file, setFile] = useState<File | null>(null);
    const [uploadMessage, setUploadMessage] = useState<string>("");
    const {currency} = useContext<ISettingsContext>(SettingsContext)
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'saveChanges') return 'Save Changes';
                break;
            case 'NL':
                if (key === 'saveChanges') return 'Wijzigingen Opslaan';
                break;
            case 'FR':
                if (key === 'saveChanges') return 'Enregistrer les modifications';
                break;
            default:
                if (key === 'saveChanges') return 'Save Changes';
                break;
        }
        return '';
    };



    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFile(e.target.files?.[0] || null);
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        changeName(
            {newName: nameValue, file: file ?? undefined},
            {
                onSuccess: (updatedPlayer) => {
                    setUploadMessage("Player updated successfully!");

                    // Update the currentPlayer state with the new name and avatar
                    if (currentPlayer) {
                        currentPlayer.name = updatedPlayer.data.name;
                        currentPlayer.avatarUrl = updatedPlayer.data.avatarUrl;
                    }

                },
                onError: () => {
                    setUploadMessage("Failed to update player.");
                },
            }
        );
    };


    return (
        <>
            <div className={"w-full h-max bg-blue-500 p-3 shadow-lg shadow-black rounded-3xl relative"}>
                <div
                    className={"absolute right-1/2 transform translate-x-1/2 rounded-full border-[5px] bg-blue-500 border-yellow z-20 w-[150px] h-[150px] flex justify-center items-center shadow-lg shadow-black cursor-pointer"}
                    onClick={() => window.open(`/profile/${currentPlayer.name}`, "_blank")}
                >
                    {currentPlayer.avatarUrl ? (
                        <img
                            className={"rounded-full z-20 w-full h-full object-cover"}
                            src={currentPlayer.avatarUrl}
                            alt={`${currentPlayer.name}'s avatar`}
                        />
                    ) : (
                        <div className={"text-white font-bold"}>
                            {currentPlayer.name}
                        </div>
                    )}
                </div>
                <div className={"border-[5px] border-yellow rounded-2xl mt-[100px] p-3 pt-[50px]"}>
                    <form onSubmit={handleSubmit}>
                        <div className="">
                            <div className="text-yellow text-4xl font-black flex items-center justify-center">
                                <CreateSharp/>
                                <input
                                    type="text"
                                    className="w-full ml-2 border-b-2 bg-transparent border-blue-500 focus:outline-none"
                                    value={nameValue}
                                    placeholder={currentPlayer.name}
                                    onChange={(e) => setNameValue(e.target.value)}
                                />
                            </div>

                            <div>
                                <input
                                    className="block w-full mx-auto text-sm text-yellow border-2 border-yellow rounded-lg cursor-pointer my-2"
                                    type="file" onChange={handleFileChange}/>
                            </div>
                        </div>
                        <div className={"w-full flex justify-center"}>
                            <button
                                className={"bg-yellow text-blue-500 font-bold p-2 shadow-black shadow-lg rounded-xl w-1/2 hover:opacity-70"}
                                type="submit">
                                {getTranslatedText('saveChanges')}
                            </button>
                        </div>

                    </form>
                    {uploadMessage && <p className="text-center mt-4">{uploadMessage}</p>}
                </div>
                <div className={"border-[5px] border-yellow rounded-2xl mt-2 p-5 grid grid-cols-1 gap-4"}>
                    <div
                        className={"w-full rounded-xl border-2 border-yellow p-2 flex items-center"}>
                        <div className={"w-2/12"}>
                            <CurrencyIcon currency={currency} />
                        </div>
                        <div className={"w-full text-white font-bold text-xl content-center"}>
                            {currentPlayer?.balance}
                        </div>
                    </div>
                    <div
                        className={"w-full rounded-xl border-2 border-yellow p-2 flex items-center"}>
                        <div className={"w-2/12"}>
                            <Star className={"float-left"} sx={{
                                width: "100%",
                                height: "100%",
                                marginY: "auto",
                                color: "#fcd34d"
                            }}/>
                        </div>
                        <div className={"w-full text-white font-bold text-xl content-center"}>
                            {currentPlayer?.xp} XP
                        </div>
                    </div>

                </div>
            </div>

        </>
    )
}

export default Account