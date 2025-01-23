import {useContext, useState} from "react";
import CurrentPlayerContext, {ICurrentPlayerContext} from "../../context/player/CurrentPlayerContext";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import {useAvailableGimmicks, usePlayerGimmicks, usePurchaseGimmick} from "../../hooks/UseGimmicks.ts";
import {Gimmick} from "../../model/Gimmick.ts";
import {BackButton} from "../general/BackButton.tsx";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";
import {GimmickListItem} from "./GimmickListItem.tsx";

export function GimmickStore() {
    const {isLoadingAvailableGimmicks, availableGimmicks, refetch: refetchAvailableGimmicks} = useAvailableGimmicks();
    const {isLoadingPlayerGimmicks, playerGimmicks, refetch: refetchPlayerGimmicks} = usePlayerGimmicks();
    const {purchase} = usePurchaseGimmick();
    const {currentPlayer} = useContext<ICurrentPlayerContext>(CurrentPlayerContext);

    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'bought') return 'Bought';
                if (key === 'failedToBuy') return 'Failed to buy';
                if (key === 'yourGimmicks') return 'Your Gimmicks';
                if (key === 'yourXp') return 'Your xp';
                if (key === 'yourBalance') return 'Your balance';
                if (key === 'buyWithXp') return 'Buy with XP';
                if (key === 'buyWithBalance') return 'Buy with Balance';
                if (key === 'noAvailableGimmicks') return 'No available gimmicks to purchase.';
                if (key === 'balance') return 'Balance';
                if (key === 'for') return 'for';
                if (key === 'no_gimmicks') return 'You don\'t own any gimmicks yet.';
                break;
            case 'NL':
                if (key === 'bought') return 'Gekocht';
                if (key === 'failedToBuy') return 'Kopen mislukt';
                if (key === 'yourGimmicks') return 'Jouw Gimmicks';
                if (key === 'yourXp') return 'Jouw xp';
                if (key === 'yourBalance') return 'Jouw balans';
                if (key === 'buyWithXp') return 'Kopen met XP';
                if (key === 'buyWithBalance') return 'Kopen met Balans';
                if (key === 'noAvailableGimmicks') return 'Geen beschikbare gadgets om te kopen.';
                if (key === 'balance') return 'Balans';
                if (key === 'for') return 'voor';
                if (key === 'no_gimmicks') return 'Je hebt nog geen gadgets.';
                break;
            case 'FR':
                if (key === 'bought') return 'Acheté';
                if (key === 'failedToBuy') return 'Impossible d\'acheter';
                if (key === 'yourGimmicks') return 'Vos gadgets';
                if (key === 'yourXp') return 'Votre xp';
                if (key === 'yourBalance') return 'Votre solde';
                if (key === 'buyWithXp') return 'Acheter avec XP';
                if (key === 'buyWithBalance') return 'Acheter avec Solde';
                if (key === 'noAvailableGimmicks') return 'Pas de gadgets disponibles à acheter.';
                if (key === 'balance') return 'Solde';
                if (key === 'for') return 'pour';
                if (key === 'no_gimmicks') return 'Vous n\'avez pas encore de gadgets.';
                break;
            default:
                if (key === 'bought') return 'Bought';
                if (key === 'failedToBuy') return 'Failed to buy';
                if (key === 'yourGimmicks') return 'Your Gimmicks';
                if (key === 'yourXp') return 'Your xp';
                if (key === 'yourBalance') return 'Your balance';
                if (key === 'buyWithXp') return 'Buy with XP';
                if (key === 'buyWithBalance') return 'Buy with Balance';
                if (key === 'noAvailableGimmicks') return 'No available gimmicks to purchase.';
                if (key === 'balance') return 'Balance';
                if (key === 'for') return 'for';
                if (key === 'no_gimmicks') return 'You don\'t own any gimmicks yet.';
                break;
        }
        return key
    }

        const handlePurchase = async (gimmickId: string, gimmickName: string, gimmickCost: number, costType: string) => {
            purchase(
                {gimmickId, type: costType},
                {
                    onSuccess: () => {
                        setSnackbarMessage(`${getTranslatedText('bought')} ${gimmickName} ${getTranslatedText('for')} ${gimmickCost} ${costType === "XP" ? getTranslatedText('xp') : getTranslatedText('balance')}`);
                        setSnackbarOpen(true);
                        refetchPlayerGimmicks();
                        refetchAvailableGimmicks();
                    },
                    onError: () => {
                        setSnackbarMessage(`${getTranslatedText('failedToBuy')} ${gimmickName}`);
                        setSnackbarOpen(true);
                    },
                }
            );
        };


        const handleSnackbarClose = () => {
            setSnackbarOpen(false);
        };

        if (isLoadingAvailableGimmicks || isLoadingPlayerGimmicks) {
            return <div className="flex items-center justify-center min-h-screen bg-gray-100">Loading...</div>;
        }

        return (
            <div className="grid grid-cols-3 gap-10 content-center p-10 pt-28 h-screen">
                {/* Player Gimmicks Section */}
                <div className="w-full rounded-3xl shadow-lg p-6 bg-blue-500">
                    <h1 className="text-yellow font-bold text-4xl mb-7">{getTranslatedText('yourGimmicks')}</h1>
                    {currentPlayer && (
                        <div className="text-white text-lg mb-4">
                            <p>
                                <strong>{getTranslatedText('yourXp')}:</strong> {currentPlayer.xp ?? 0}
                            </p>
                            <p>
                                <strong>{getTranslatedText('yourBalance')}:</strong> {currentPlayer.balance ?? 0}
                            </p>
                        </div>
                    )}
                    <div className="space-y-4 overflow-auto no-scrollbar h-[300px]">
                        {playerGimmicks?.length > 0 ? (
                            playerGimmicks.map((gimmick: Gimmick) => (
                                <GimmickListItem key={gimmick.id} gimmick={gimmick}/>
                            ))
                        ) : (
                            <p className="text-white">{getTranslatedText('no_gimmicks')}</p>
                        )}
                    </div>
                </div>

                {/* Available Gimmicks Section */}
                <div className="col-span-2">
                    <Snackbar
                        open={snackbarOpen}
                        autoHideDuration={5000}
                        onClose={handleSnackbarClose}
                        anchorOrigin={{vertical: "top", horizontal: "center"}}
                    >
                        <Alert onClose={handleSnackbarClose} severity="success" sx={{width: "100%"}}>
                            {snackbarMessage}
                        </Alert>
                    </Snackbar>

                    <div className="rounded-2xl h-[490px] relative overflow-y-scroll no-scrollbar mb-2">
                        <ul className="space-y-4">
                            {availableGimmicks?.length > 0 ? (
                                availableGimmicks.map((gimmick: Gimmick) => (
                                    <li
                                        key={gimmick.id}
                                        className="p-4 bg-blue-500 rounded-2xl shadow-md shadow-black"
                                    >
                                        <div className="flex items-center gap-4">
                                            <img
                                                src={gimmick.imageUrl}
                                                alt={gimmick.name}
                                                className="w-16 h-16 rounded-full object-cover"
                                            />
                                            <div>
                                                <p className="text-lg text-yellow font-medium">{gimmick.name}</p>
                                                <p className="text-green-500 font-bold">{gimmick.xpCost} XP</p>
                                                <p className="text-red-500 font-bold">{gimmick.balanceCost} {getTranslatedText('balance')}</p>
                                            </div>
                                        </div>
                                        <div className="flex space-x-4 mt-4">
                                            <button
                                                className="bg-yellow text-blue-500 font-bold p-2 shadow-black shadow-lg rounded-xl w-1/3 hover:opacity-70"
                                                onClick={() => handlePurchase(gimmick.id, gimmick.name, gimmick.xpCost, "XP")}
                                            >
                                                {getTranslatedText('buyWithXp')}
                                            </button>
                                            <button
                                                className="bg-yellow text-blue-500 font-bold p-2 shadow-black shadow-lg rounded-xl w-1/3 hover:opacity-70"
                                                onClick={() => handlePurchase(gimmick.id, gimmick.name, gimmick.balanceCost, "balance")}
                                            >
                                                {getTranslatedText('buyWithBalance')}
                                            </button>
                                        </div>
                                    </li>
                                ))
                            ) : (
                                <p className="text-gray-500">{getTranslatedText('noAvailableGimmicks')}</p>
                            )}
                        </ul>
                    </div>
                    <BackButton path="/" />
                </div>
            </div>
        );
    }



