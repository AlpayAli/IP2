import {useNavigate, useParams} from "react-router-dom";
import Account from "../general/Account.tsx";
import {BackButton} from "../general/BackButton.tsx";
import {useInvite} from "../../hooks/useInvite.ts";
import {useContext} from "react";
import CurrentPlayerContext, {ICurrentPlayerContext} from "../../context/player/CurrentPlayerContext.ts";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function Invites() {
    const navigate = useNavigate()
    const {friends, isLoadingFriends, isErrorLoadingFriends, invite, inviteAll} = useInvite()

    const {id} = useParams()
    const {currentPlayer} = useContext<ICurrentPlayerContext>(CurrentPlayerContext);

    const { language } = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'byUsername') return 'By username or email';
                if (key === 'invites') return 'Invites';
                if (key === 'friends') return 'Friends';
                if (key === 'loadingFriends') return 'Loading friends...';
                if (key === 'error') return 'Something went wrong...';
                if (key === 'noFriends') return 'No friends found...';
                if (key === 'inviteAll') return 'Invite all friends';
                if (key === 'goToGame') return 'Go to Game';
                if (key === 'invite') return 'Invite';
                if (key === 'username') return 'Username';
                break;
            case 'NL':
                if (key === 'byUsername') return 'Op naam of mailadres';
                if (key === 'invites') return 'Uitnodigingen';
                if (key === 'friends') return 'Vrienden';
                if (key === 'loadingFriends') return 'Vrienden laden...';
                if (key === 'error') return 'Er is iets misgegaan...';
                if (key === 'noFriends') return 'Geen vrienden gevonden...';
                if (key === 'inviteAll') return 'Nodig alle vrienden uit';
                if (key === 'goToGame') return 'Ga naar Spel';
                if (key === 'invite') return 'Uitnodigen';
                if (key === 'username') return 'Gebruikersnaam';
                break;
            case 'FR':
                if (key === 'byUsername') return 'Par nom de jouez ou mail';
                if (key === 'invites') return 'Invitations';
                if (key === 'friends') return 'Amis';
                if (key === 'loadingFriends') return 'Chargement des amis...';
                if (key === 'error') return 'Quelque chose a mal tourné...';
                if (key === 'noFriends') return 'Aucun ami trouvé...';
                if (key === 'inviteAll') return 'Inviter tous les amis';
                if (key === 'goToGame') return 'Aller au jeu';
                if (key === 'invite') return 'Inviter';
                if (key === 'username') return "Nom d'utilisateur";
                break;
            default:
                if (key === 'byUsername') return 'By username or email';
                if (key === 'invites') return 'Invites';
                if (key === 'friends') return 'Friends';
                if (key === 'loadingFriends') return 'Loading friends...';
                if (key === 'error') return 'Something went wrong...';
                if (key === 'noFriends') return 'No friends found...';
                if (key === 'inviteAll') return 'Invite all friends';
                if (key === 'goToGame') return 'Go to Game';
                if (key === 'invite') return 'Invite';
                if (key === 'username') return 'Username';
        }
        return '';
    };

    return (
        <>
            <div className={"grid grid-cols-3 gap-10 content-center p-10 pt-28 h-screen"}>

                <div className={""}>

                    {currentPlayer ?
                        <Account currentPlayer={currentPlayer}/>
                        :
                        <div className={"w-full h-full bg-blue-500 p-3 shadow-lg shadow-black rounded-3xl relative"}>
                            ERROR
                        </div>
                    }
                </div>
                <div className={"h-full w-full col-span-2 bg-blue-500 p-5 rounded-2xl relative shadow-lg shadow-black"}>
                    <h1 className={"text-yellow font-bold text-4xl mb-7"}>{getTranslatedText("invites")}</h1>

                    <div className={" grid grid-cols-2 gap-5 p-0 "}>
                        <div
                            className={"border-4 border-yellow rounded-2xl p-2 relative overflow-y-scroll no-scrollbar "}>
                            <p className={"text-white text-center text-2xl font-bold"}>{getTranslatedText("friends")}</p>
                            {isLoadingFriends && <p>{getTranslatedText("loadingFriends")}</p>}
                            {isErrorLoadingFriends && <p>{getTranslatedText("error")}</p>}
                            {friends.length === 0 && <p className={"text-white"}>{getTranslatedText("noFriends")}</p>}
                            {friends.map((friend) => (
                                <div className={"flex justify-between border-b-2 p-1 border-yellow"}
                                     key={friend.nickname}>
                                    <div className={"text-yellow font-bold"}>{friend.nickname}</div>
                                    <div className={""}>
                                        <button
                                            className={"bg-yellow shadow-lg shadow-gray-500 font-bold p-1 rounded-xl text-blue-500 hover:opacity-70"}
                                            onClick={
                                                () => {
                                                    if (id) {
                                                        invite({gameId: id, username: friend.nickname})
                                                    }
                                                }}>
                                            {getTranslatedText("invite")}
                                        </button>
                                    </div>
                                </div>
                            ))}

                            <button
                                className={"absolute bottom-4 bg-yellow text-blue-500 font-bold p-2 shadow-black shadow-lg rounded-xl hover:opacity-70"}
                                onClick={() => {
                                    if (id) {
                                        inviteAll({gameId: id})
                                    }
                                }}>
                                {getTranslatedText("inviteAll")}
                            </button>
                        </div>
                        <div className={"border-4 border-yellow  rounded-2xl p-4 h-[300px]"}>
                            <p className={"text-white text-center text-2xl font-bold"}>{getTranslatedText("byUsername")}</p>
                            <div className={"flex mt-20"}>
                                <form className={"grid grid-cols-7 gap-2"}
                                      onSubmit={(e) => {
                                          e.preventDefault()
                                          if (id) {
                                              // @ts-expect-error username is not a valid property but it works
                                              invite({gameId: id, username: e.target.username.value})
                                          }
                                      }
                                      }
                                >
                                    <input type="text" name={"username"}
                                           className="col-span-6 bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                           placeholder={getTranslatedText("username")}
                                    />
                                    <button
                                        className={"bg-yellow shadow-lg shadow-gray-500 font-bold p-1 rounded-xl text-blue-500 hover:opacity-70"}

                                        type={"submit"}>{getTranslatedText("invite")}
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div className={"absolute w-[500px] bottom-10"}>
                        <BackButton path={"/"}/>
                        <button
                            className={"mt-4 ml-2 bg-yellow text-blue-500 font-bold p-2 shadow-black shadow-lg rounded-xl w-1/3 hover:opacity-70"}
                            onClick={() => navigate(`/game/${id}`)}>
                            {getTranslatedText("goToGame")}
                        </button>

                    </div>

                </div>
            </div>
        </>
    )
}