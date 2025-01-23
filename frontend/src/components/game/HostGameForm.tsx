import { GameData } from "../../model/GameRequest.ts";
import { z } from "zod";
import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { MenuItem, TextField } from "@mui/material";
import {BackButton} from "../general/BackButton.tsx";
import {useContext} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";


export interface HostGameFormProps {
    onSubmit: (game: GameData) => void;
}

const addGameSchema: z.ZodType<GameData> = z.object({
    minPlayers: z.string(),
    maxPlayers: z.string(),
    smallBlind: z.string(),
    security: z.enum(["public", "private"]),
});

export function HostGameForm({ onSubmit }: HostGameFormProps) {
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'hostGame') return 'Host Game';
                if (key === 'minimumPlayers') return 'Minimum Players';
                if (key === 'maximumPlayers') return 'Maximum Players';
                if (key === 'smallBlind') return 'Small Blind';
                if (key === 'type') return 'Type';
                if (key === 'public') return 'Public';
                if (key === 'private') return 'Private';
                if (key === 'createGame') return 'Create Game';
                break;
            case 'NL':
                if (key === 'hostGame') return 'Spel hosten';
                if (key === 'minimumPlayers') return 'Minimum aantal spelers';
                if (key === 'maximumPlayers') return 'Maximaal aantal spelers';
                if (key === 'smallBlind') return 'Small Blind';
                if (key === 'type') return 'Type';
                if (key === 'public') return 'Openbaar';
                if (key === 'private') return 'Privé';
                if (key === 'createGame') return 'Spel aanmaken';
                break;
            case 'FR':
                if (key === 'hostGame') return 'Héberger un jeu';
                if (key === 'minimumPlayers') return 'Nombre minimum de joueurs';
                if (key === 'maximumPlayers') return 'Nombre maximum de joueurs';
                if (key === 'smallBlind') return 'Petite blinde';
                if (key === 'type') return 'Type';
                if (key === 'public') return 'Public';
                if (key === 'private') return 'Privé';
                if (key === 'createGame') return 'Créer un jeu';
                break;
            default:
                if (key === 'hostGame') return 'Host Game';
                if (key === 'minimumPlayers') return 'Minimum Players';
                if (key === 'maximumPlayers') return 'Maximum Players';
                if (key === 'smallBlind') return 'Small Blind';
                if (key === 'type') return 'Type';
                if (key === 'public') return 'Public';
                if (key === 'private') return 'Private';
                if (key === 'createGame') return 'Create Game';
                break;
        }
        return '';
    }

    const {
        handleSubmit,
        control,
        formState: { errors },
    } = useForm<GameData>({
        resolver: zodResolver(addGameSchema),
        defaultValues: {
            minPlayers: "2",
            maxPlayers: "9",
            smallBlind: "5",
            security: "public",
        },
    });

    return (
        <form
            className={""}
            onSubmit={handleSubmit((data) => {
                onSubmit(data);
            })}
        >
            <h1 className={"text-yellow font-bold text-4xl mb-7"}>{getTranslatedText('hostGame')}</h1>
            <div className={"my-2 grid grid-cols-1"}>
                <Controller
                    name="minPlayers"
                    control={control}
                    render={({field}) => (
                        <TextField
                            {...field}
                            label={getTranslatedText('minimumPlayers')}
                            type={"number"}
                            inputProps={{
                                min: 2,
                                max: 9,
                            }}
                            error={!!errors.minPlayers}
                            helperText={errors.minPlayers?.message}
                        />
                    )}
                />
            </div>
            <div className={"my-2 grid grid-cols-1"}>
                <Controller
                    name="maxPlayers"
                    control={control}
                    render={({field}) => (
                        <TextField
                            {...field}
                            label={getTranslatedText('maximumPlayers')}
                            type={"number"}
                            inputProps={{
                                min: 2,
                                max: 9,
                            }}
                            error={!!errors.maxPlayers}
                            helperText={errors.maxPlayers?.message}
                        />
                    )}
                />
            </div>

            <div className={"my-2 grid grid-cols-1"}>
                <Controller
                    name="smallBlind"
                    control={control}
                    render={({field}) => (
                        <TextField
                            {...field}
                            label={getTranslatedText('smallBlind')}
                            type={"number"}
                            inputProps={{
                                min: 5,
                            }}
                            error={!!errors.smallBlind}
                            helperText={errors.smallBlind?.message}
                        />
                    )}
                />
            </div>

            <div className={"my-2 grid grid-cols-1"}>
                <Controller
                    name="security"
                    control={control}
                    render={({field}) => (
                        <TextField
                            className={"w-full"}
                            {...field}
                            select
                            label={getTranslatedText('type')}
                            error={!!errors.security}
                            helperText={errors.security?.message}
                        >
                            <MenuItem key={"public"} value={"public"}>
                                {getTranslatedText('public')}
                            </MenuItem>
                            <MenuItem key={"private"} value={"private"}>
                                {getTranslatedText('private')}
                            </MenuItem>
                        </TextField>
                    )}
                />
            </div>

            <div className={"absolute w-[500px] bottom-10"}>

                <BackButton path={"/"}/>

                <button
                    className={"mt-4 ml-2 bg-yellow text-blue-500 font-bold p-2 shadow-black shadow-lg rounded-xl w-1/3 hover:opacity-70"}
                    type={"submit"}>
                    {getTranslatedText('createGame')}
                </button>
            </div>
        </form>
);
}
