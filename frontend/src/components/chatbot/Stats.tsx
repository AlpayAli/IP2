import {useContext, useState} from "react";
import { useCalculateStats } from "../../hooks/useChatBot";
import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {  TextField, FormControl, FormLabel, Checkbox, FormGroup, FormControlLabel } from "@mui/material";
import { z } from "zod";
import SettingsContext, { ISettingsContext } from "../../context/settings/SettingsContext";

class StatsRequest {
    minPlayers: number;
    maxPlayers: number;
    playTime: number;
    minAge: number;
    usersRated: number;
    domains: string[];
    mechanics: string[];
    constructor(
        minPlayers: number,
        maxPlayers: number,
        playTime: number,
        minAge: number,
        usersRated: number,
        domains: string[],
        mechanics: string[]
    ) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playTime = playTime;
        this.minAge = minAge;
        this.usersRated = usersRated;
        this.domains = domains;
        this.mechanics = mechanics;
    }
}



export function Stats() {
    const { language } = useContext<ISettingsContext>(SettingsContext);
    const [statsResponse, setStatsResponse] = useState<any>(null);

    const getTranslatedText = (key: string): string => {
        switch (language) {
            case "EN":
                if (key === "stats") return "Stats";
                if (key === "selectDomains") return "Select Domains";
                if (key === "selectMechanics") return "Select Mechanics";
                if (key === "maxPlayers") return "Max Players";
                if (key === "minPlayers") return "Min Players";
                if (key === "playTime") return "Play Time";
                if (key === "minAge") return "Min Age";
                if (key === "usersRated") return "Users Rated";
                if (key === "calculateStats") return "Calculate Stats";
                break;
            case "NL":
                if (key === "stats") return "Statistieken";
                if (key === "selectDomains") return "Selecteer Domeinen";
                if (key === "selectMechanics") return "Selecteer Mechanismen";
                if (key === "maxPlayers") return "Max Spelers";
                if (key === "minPlayers") return "Min Spelers";
                if (key === "playTime") return "Speeltijd";
                if (key === "minAge") return "Min Leeftijd";
                if (key === "usersRated") return "Gebruikers Beoordeeld";
                if (key === "calculateStats") return "Bereken Statistieken";
                break;
            case "FR":
                if (key === "stats") return "Statistiques";
                if (key === "selectDomains") return "Sélectionner des domaines";
                if (key === "selectMechanics") return "Sélectionner des mécaniques";
                if (key === "maxPlayers") return "Max Joueurs";
                if (key === "minPlayers") return "Min Joueurs";
                if (key === "playTime") return "Temps de jeu";
                if (key === "minAge") return "Âge minimum";
                if (key === "usersRated") return "Utilisateurs Notés";
                if (key === "calculateStats") return "Calculer les statistiques";
                break;
            default:
                if (key === "stats") return "Statistieken";
                if (key === "selectDomains") return "Selecteer Domeinen";
                if (key === "selectMechanics") return "Selecteer Mechanismen";
                if (key === "maxPlayers") return "Max Spelers";
                if (key === "minPlayers") return "Min Spelers";
                if (key === "playTime") return "Speeltijd";
                if (key === "minAge") return "Min Leeftijd";
                if (key === "usersRated") return "Gebruikers Beoordeeld";
                if (key === "calculateStats") return "Bereken Statistieken";
                break;
        }
        return key;
    };

    const availableDomains = [
        "Abstract Games",
        "Children's Games",
        "Customizable Games",
        "Family Games",
        "Party Games",
        "Strategy Games",
        "Thematic Games",
        "Wargames",
        "UNKNOWN",
    ];

    const availableMechanics = [
        'Hand Management',
        'Income',
        'Trading',
        'Market',
        'Auction/Bidding',
        'Deck Bag and Pool Building',
        'Deck Construction',
        'Delayed Purchase',
        'Loans',
        'Investment',
        'Contracts',
        'Tile Placement',
        'Worker Placement',
        'Worker Placement with Dice Workers',
        'Commodity Speculation',
        'Static Capture',
        'Increase Value of Unchosen Resources',
        'Area Majority / Influence',
        'Area Movement',
        'Area-Impulse',
        'Combat',
        'Command Cards',
        'Dice Rolling',
        'Re-rolling and Locking',
        'Kill Steal',
        'Take That',
        'King of the Hill',
        'Role Playing',
        'Secret Unit Deployment',
        'Tug of War',
        'Zone of Control',
        'Ratio / Combat Results Table',
        'Predictive Bid',
        'Bidding',
        'Auction: Dutch',
        'Auction: English',
        'Cooperative Game',
        'Semi-Cooperative Game',
        'Team-Based Game',
        'Negotiation',
        'Alliances',
        'Force Commitment',
        'Roles with Asymmetric Information',
        'Pattern Building',
        'Set Collection',
        'Pattern Recognition',
        'Puzzle Solving',
        'Mancala',
        'Grid Coverage',
        'Hexagon Grid',
        'Enclosure',
        'Path Building',
        'Measurement Movement',
        'Line Drawing',
        'Action Points',
        'Action Queue',
        'Programming',
        'Simultaneous Action Selection',
        'Deck Building',
        'Variable Phase Order',
        'Tech Trees / Tech Tracks',
        'Variable Player Powers',
        'Legacy Game',
        'Finale Ending',
        'Scenario / Mission / Campaign Game',
        'Map Exploration',
        'Campaign / Battle Card Driven',
        'Narrative Choice / Paragraph',
        'Storytelling',
        'Hidden Movement',
        'Map Reduction',
        'Map Addition',
        'Bluffing',
        'Hidden Roles',
        'Voting',
        'Singing',
        'Bribery',
        'Player Judge',
        'Hot Potato',
        'Traitor Game',
        'Push Your Luck',
        'Random Production',
        'Drawing',
        'Die Icon Resolution',
        'Critical Hits and Failures',
        'Rock-Paper-Scissors',
        'Betting and Bluffing',
        'Unknown',
        'Advantage Token',
        'Bias',
        'Elapsed Real Time Ending',
        'Single Loser Game',
        'Passed Action Token',
        'I Cut You Choose',
        'Action Timer'
    ];



    const { calculateStatsAsync } = useCalculateStats();

    const {
        handleSubmit,
        control,
        formState: { errors },
        setValue,
    } = useForm<StatsRequest>({
        resolver: zodResolver(
            z.object({
                minPlayers: z.number().min(0),
                maxPlayers: z.number().min(0),
                playTime: z.number().min(0),
                minAge: z.number().min(0),
                usersRated: z.number().min(0),
                domains: z.array(z.string()),
                mechanics: z.array(z.string()),
            })
        ),
        defaultValues: {
            minPlayers: 0,
            maxPlayers: 0,
            playTime: 0,
            minAge: 0,
            usersRated: 0,
            domains: [],
            mechanics: [],
        },
    });

    const onSubmit = async (data: StatsRequest) => {
        const statsRequest = new StatsRequest(
            data.minPlayers,
            data.maxPlayers,
            data.playTime,
            data.minAge,
            data.usersRated,
            data.domains,
            data.mechanics
        );
        try {
            const response = await calculateStatsAsync(statsRequest);
            setStatsResponse(response);
        } catch (error) {
            console.error("Error calculating stats:", error);
        }
    };

    return (
        <div className={"col-span-2 bg-blue-500 rounded-2xl shadow-lg shadow-black p-10 relative"}>
            <form onSubmit={handleSubmit(onSubmit)}>
                <h1 className="text-yellow font-bold text-4xl mb-7">{getTranslatedText("stats")}</h1>
                <div className="my-2 grid grid-cols-1">
                    <Controller
                        name="minPlayers"
                        control={control}
                        render={({field}) => (
                            <TextField
                                {...field}
                                label={getTranslatedText("minPlayers")}
                                onChange={(e) => field.onChange(Number(e.target.value))}
                                type="number"
                                inputProps={{min: 0}}
                                error={!!errors.minPlayers}
                                helperText={errors.minPlayers?.message}
                            />
                        )}
                    />
                </div>
                <div className="my-2 grid grid-cols-1">
                    <Controller
                        name="maxPlayers"
                        control={control}
                        render={({field}) => (
                            <TextField
                                {...field}
                                label={getTranslatedText("maxPlayers")}
                                onChange={(e) => field.onChange(Number(e.target.value))}
                                type="number"
                                inputProps={{min: 0}}
                                error={!!errors.maxPlayers}
                                helperText={errors.maxPlayers?.message}
                            />
                        )}
                    />
                </div>

                <div className="my-2 grid grid-cols-1">
                    <Controller
                        name="playTime"
                        control={control}
                        render={({field}) => (
                            <TextField
                                {...field}
                                label={getTranslatedText("playTime")}
                                type="number"
                                inputProps={{min: 0}}
                                onChange={(e) => field.onChange(Number(e.target.value))}
                                error={!!errors.playTime}
                                helperText={errors.playTime?.message}
                            />
                        )}
                    />
                </div>

                <div className="my-2 grid grid-cols-1">
                    <Controller
                        name="minAge"
                        control={control}
                        render={({field}) => (
                            <TextField
                                {...field}
                                label={getTranslatedText("minAge")}
                                type="number"
                                inputProps={{min: 0}}
                                error={!!errors.minAge}
                                onChange={(e) => field.onChange(Number(e.target.value))}
                                helperText={errors.minAge?.message}
                            />
                        )}
                    />
                </div>

                <div className="my-2 grid grid-cols-1">
                    <Controller
                        name="usersRated"
                        control={control}
                        render={({field}) => (
                            <TextField
                                {...field}
                                label={getTranslatedText("usersRated")}
                                type="number"
                                onChange={(e) => field.onChange(Number(e.target.value))}
                                inputProps={{min: 0}}
                                error={!!errors.usersRated}
                                helperText={errors.usersRated?.message}
                            />
                        )}
                    />
                </div>

                <FormControl className="my-2">
                    <FormLabel>{getTranslatedText("selectDomains")}</FormLabel>
                    <div className={"overflow-auto h-[300px]"}>
                        <FormGroup>
                            {availableDomains.map((domain) => (
                                <FormControlLabel
                                    key={domain}
                                    control={
                                        <Controller
                                            name="domains"
                                            control={control}
                                            render={({field}) => (
                                                <Checkbox
                                                    {...field}
                                                    value={domain}
                                                    onChange={() => {
                                                        const currentDomains = field.value || [];
                                                        if (currentDomains.includes(domain)) {
                                                            setValue("domains", currentDomains.filter((item) => item !== domain));
                                                        } else {
                                                            setValue("domains", [...currentDomains, domain]);
                                                        }
                                                    }}
                                                />
                                            )}
                                        />
                                    }
                                    label={domain}
                                />
                            ))}
                        </FormGroup>
                    </div>
                </FormControl>

                <FormControl className="my-2">
                    <FormLabel>{getTranslatedText("selectMechanics")}</FormLabel>
                    <div className={"overflow-auto h-[300px]"}>
                    <FormGroup>
                        {availableMechanics.map((mechanic) => (
                            <FormControlLabel
                                key={mechanic}
                                control={
                                    <Controller
                                        name="mechanics"
                                        control={control}
                                        render={({field}) => (
                                            <Checkbox
                                                {...field}
                                                value={mechanic}
                                                onChange={() => {
                                                    const currentMechanics = field.value || [];
                                                    if (currentMechanics.includes(mechanic)) {
                                                        setValue("mechanics", currentMechanics.filter((item) => item !== mechanic));
                                                    } else {
                                                        setValue("mechanics", [...currentMechanics, mechanic]);
                                                    }
                                                }}
                                            />
                                        )}
                                    />
                                }
                                label={mechanic}
                            />
                        ))}
                    </FormGroup>
                </div>
                </FormControl>

                <button
                    type="submit"
                    className="mt-4 ml-2 bg-yellow text-blue-500 font-bold p-2 w-[120px] shadow-black shadow-lg rounded-xl w-1/3 hover:opacity-70"
                >
                    {getTranslatedText("calculateStats")}
                </button>
            </form>
            {statsResponse && (
                <div>
                    <h3>Stats Response</h3>
                    <pre>{JSON.stringify(statsResponse, null, 2)}</pre>
                </div>
            )}
        </div>
            );
            }
