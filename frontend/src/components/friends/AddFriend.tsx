import {useContext, useState} from "react";
import { useFriends } from "../../hooks/useFriends";
import { AxiosError} from "axios"
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function AddFriend() {
    const { addFriendByNickname } = useFriends();
    const [nickname, setNickname] = useState("");
    const [snackbar, setSnackbar] = useState<{ message: string; type: "success" | "error" | "" }>({
        message: "",
        type: "",
    });
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const { language } = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'addFriend') return 'Add a Friend';
                if (key === 'enterNickname') return "Enter your friend's nickname/email";
                if (key === 'addFriendButton') return 'Add Friend';
                if (key === 'snackbarAdded') {
                    if (emailRegex.test(nickname)) {
                        return `Friend request was sent to "${nickname}"!`;
                    } else {
                        return `Friend "${nickname}" added successfully!`;
                    }
                }
                if (key === 'snackbarErrorExists') {
                    if (emailRegex.test(nickname)) {
                        return `User with email "${nickname}" is already added.`;
                    } else {
                        return `User "${nickname}" is already added.`;
                    }
                }
                if (key === 'snackbarErrorSelf') return "You cannot add yourself as a friend.";
                if (key === 'snackbarErrorNotExist') {
                    if (emailRegex.test(nickname)) {
                        return `No account found linked to "${nickname}".`;
                    }
                    return `User "${nickname}" does not exist.`;
                }
                if (key === 'snackbarErrorGeneric') return "Something went wrong. Please try again.";
                break;
            case 'NL':
                if (key === 'addFriend') return 'Vriend Toevoegen';
                if (key === 'enterNickname') return "Typ de gebruikersnaam of e-mailadres van je vriend";
                if (key === 'addFriendButton') return 'Voeg Vriend Toe';
                if (key === 'snackbarAdded') {
                    if (emailRegex.test(nickname)) {
                        return `Vriendschapsverzoek verzonden naar "${nickname}"!`;
                    } else {
                        return `Vriend "${nickname}" succesvol toegevoegd!`;
                    }
                }
                if (key === 'snackbarErrorExists') {
                    if (emailRegex.test(nickname)) {
                        return `Gebruiker met e-mailadres "${nickname}" is al toegevoegd.`;
                    } else {
                        return `Gebruiker "${nickname}" is al toegevoegd.`;
                    }
                }
                if (key === 'snackbarErrorSelf') return "Je kunt jezelf niet als vriend toevoegen.";
                if (key === 'snackbarErrorNotExist') {
                    if (emailRegex.test(nickname)) {
                        return `Geen account gevonden gekoppeld aan "${nickname}".`;
                    }
                    return `Gebruiker "${nickname}" bestaat niet.`;
                }
                if (key === 'snackbarErrorGeneric') return "Er is iets mis gegaan. Probeer het opnieuw.";
                break;
            case 'FR':
                if (key === 'addFriend') return 'Ajouter un Ami';
                if (key === 'enterNickname') return "Entrez le surnom ou l'email de votre ami";
                if (key === 'addFriendButton') return 'Ajouter un Ami';
                if (key === 'snackbarAdded') {
                    if (emailRegex.test(nickname)) {
                        return `Une demande d'ami a été envoyée à "${nickname}"!`;
                    } else {
                        return `Ami "${nickname}" ajouté avec succès!`;
                    }
                }
                if (key === 'snackbarErrorExists') {
                    if (emailRegex.test(nickname)) {
                        return `L'utilisateur avec l'adresse email "${nickname}" est déjà ajouté.`;
                    } else {
                        return `L'utilisateur "${nickname}" est déjà ajouté.`;
                    }
                }
                if (key === 'snackbarErrorSelf') return "Vous ne pouvez pas vous ajouter en tant qu'ami.";
                if (key === 'snackbarErrorNotExist') {
                    if (emailRegex.test(nickname)) {
                        return `Aucun compte trouvé lié à "${nickname}".`;
                    }
                    return `L'utilisateur "${nickname}" n'existe pas.`;
                }
                if (key === 'snackbarErrorGeneric') return "Quelque chose s'est mal passé. Veuillez réessayer.";
                break;
            default:
                if (key === 'addFriend') return 'Add a Friend';
                if (key === 'enterNickname') return "Enter your friend's nickname/email";
                if (key === 'addFriendButton') return 'Add Friend';
                if (key === 'snackbarAdded') {
                    if (emailRegex.test(nickname)) {
                        return `Friend request was sent to "${nickname}"!`;
                    } else {
                        return `Friend "${nickname}" added successfully!`;
                    }
                }
                if (key === 'snackbarErrorExists') {
                    if (emailRegex.test(nickname)) {
                        return `User with email "${nickname}" is already added.`;
                    } else {
                        return `User "${nickname}" is already added.`;
                    }
                }
                if (key === 'snackbarErrorSelf') return "You cannot add yourself as a friend.";
                if (key === 'snackbarErrorNotExist') {
                    if (emailRegex.test(nickname)) {
                        return `No account found linked to "${nickname}".`;
                    }
                    return `User "${nickname}" does not exist.`;
                }
                if (key === 'snackbarErrorGeneric') return "Something went wrong. Please try again.";
                break;
        }
        return '';
    };
    // const {language} = useContext<ISettingsContext>(SettingsContext);
    const handleAddFriend = () => {
        if (!nickname.trim()) return;
        try {
            addFriendByNickname(nickname, {
                onSuccess: () => {
                    setSnackbar({
                        message: `Friend "${nickname}" added successfully!`,
                        type: "success",
                    });
                },
                onError: (error : Error) => {
                    let errorMessage;
                    const axiosError = error as AxiosError
                    if (axiosError.status === 404) {
                        errorMessage = getTranslatedText('snackbarErrorNotExist');
                    } else if (axiosError.status === 400) {
                        if (axiosError.response?.data === "Player cannot send friend request to themselves") {
                            errorMessage = getTranslatedText('snackbarErrorSelf');
                        } else {
                            errorMessage = getTranslatedText('snackbarErrorExists');
                        }
                    } else {
                        errorMessage = getTranslatedText('snackbarErrorGeneric');
                    }
                    setSnackbar({
                        message: errorMessage,
                        type: "error",
                    });
                },
            });
        } catch {
            setSnackbar({
                message: getTranslatedText('snackbarErrorGeneric'),
                type: "error",
            });
        }

        setNickname("");
        setTimeout(() => {
            setSnackbar({ message: "", type: "" });
        }, 3000); // Snackbar verdwijnt na 3 seconden
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === "Enter") {
            handleAddFriend();
        }
    };

    return (
        <div className="p-4">
            <div className="w-full max-w-md p-6 rounded-3xl">
                <h1 className="text-2xl font-bold mb-4 text-blue-500">{getTranslatedText('addFriend')}</h1>
                <div className="flex flex-col space-y-4">
                    <input
                        type="text"
                        placeholder={getTranslatedText('enterNickname')}
                        value={nickname}
                        onChange={(e) => setNickname(e.target.value)}
                        onKeyDown={handleKeyDown}
                        className="p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                    />
                    <button
                        onClick={handleAddFriend}
                        className={`p-2 text-yellow bg-blue-500 rounded text-bold ${
                            !nickname.trim() ? "opacity-50" : "hover:opacity-70"
                        }`}
                        disabled={!nickname.trim()}
                    >
                        {getTranslatedText('addFriendButton')}
                    </button>
                </div>
            </div>

            {/* Snackbar */}
            {snackbar.type && (
                <div
                    className={`fixed top-4 left-1/2 transform -translate-x-1/2 py-2 px-4 rounded shadow-lg bg-blue-500 text-white`}
                >
                    {
                        // We render the message, replacing the nickname with a yellow span
                        snackbar.message.split('"').map((part, index) => (
                            <span key={index}>
                                {index === 1 ? (
                                    <span className="text-yellow font-bold">{part}</span>
                                ) : (
                                    part
                                )}
                            </span>
                        ))
                    }
                </div>
            )}
        </div>
    );
}
