import {useContext, useEffect, useState} from "react";
import {useDailySpin} from "../../hooks/useDailySpin.ts";
import {playSound} from "../../utils/SoundUtils.ts";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function WheelOfFortune() {
    const [spinning, setSpinning] = useState(false);
    const [selectedPrize, setSelectedPrize] = useState<number | null>(null);
    const {spin, isSpun, spinData} = useDailySpin()
    const {soundLevel} = useContext<ISettingsContext>(SettingsContext);
    const {language} = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'spinWheel') return 'Spin the Wheel';
                if (key === 'spinning') return 'Spinning...';
                if (key === 'congratulations') return 'Congratulations! You won';
                if (key === 'coins') return 'coins!';
                break;
            case 'NL':
                if (key === 'spinWheel') return 'Draai aan het wiel';
                if (key === 'spinning') return 'Bezig met draaien...';
                if (key === 'congratulations') return 'Gefeliciteerd! Je won';
                if (key === 'coins') return 'munten!';
                break;
            case 'FR':
                if (key === 'spinWheel') return 'Tourne la roue';
                if (key === 'spinning') return 'En train de tourner...';
                if (key === 'congratulations') return 'Félicitations! Vous avez gagné';
                if (key === 'coins') return 'pièces!';
                break;
            default:
                if (key === 'spinWheel') return 'Spin the Wheel';
                if (key === 'spinning') return 'Spinning...';
                if (key === 'congratulations') return 'Congratulations! You won';
                if (key === 'coins') return 'coins!';
                break;
        }
        return '';
    };

    useEffect(() => {
        if (isSpun && spinData) {
            spinWheel(spinData)
        }
    }, [isSpun, spinData]);

    const prizes = [10, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500];

    const spinWheel = (spin: number) => {
        if (spinning) return;

        setSpinning(true);
        setSelectedPrize(null);

        const randomIndex = Math.floor(Math.random() * prizes.length);
        const rotation = 360 * 3 + (randomIndex * (360 / prizes.length)); // 3 full spins + prize angle

        const wheel = document.getElementById('wheel');
        if (wheel) {
            wheel.style.transition = 'transform 4s ease-out';
            wheel.style.transform = `rotate(${rotation}deg)`;
        }

        setTimeout(() => {
            setSpinning(false);
            setSelectedPrize(spin);

            // Reset wheel rotation for the next spin
            if (wheel) {
                wheel.style.transition = 'none';
                wheel.style.transform = `rotate(${(randomIndex * (360 / prizes.length))}deg)`;
            }
            playSound("https://storage.googleapis.com/image_bucket_ip2/sounds/dailyspin.wav", soundLevel)

        }, 4000);
    };

    return (
        <div className="flex flex-col items-center justify-center">
            <div className="relative w-96 h-96">
                <div
                    id="wheel"
                    className={`${!spinning && "animate-pulse "} w-full h-full rounded-full border-4 border-gray-800 flex justify-center items-center`}
                    style={{
                        background: `conic-gradient(
          #e63946 0deg, #e63946 ${(360 / 11) - 1}deg,
          #f4a261 ${(360 / 11)}deg, #f4a261 ${(2 * 360 / 11) - 1}deg,
          #2a9d8f ${(2 * 360 / 11)}deg, #2a9d8f ${(3 * 360 / 11) - 1}deg,
          #264653 ${(3 * 360 / 11)}deg, #264653 ${(4 * 360 / 11) - 1}deg,
          #457b9d ${(4 * 360 / 11)}deg, #457b9d ${(5 * 360 / 11) - 1}deg,
          #1d3557 ${(5 * 360 / 11)}deg, #1d3557 ${(6 * 360 / 11) - 1}deg,
          #e76f51 ${(6 * 360 / 11)}deg, #e76f51 ${(7 * 360 / 11) - 1}deg,
          #8ab17d ${(7 * 360 / 11)}deg, #8ab17d ${(8 * 360 / 11) - 1}deg,
          #f4e285 ${(8 * 360 / 11)}deg, #f4e285 ${(9 * 360 / 11) - 1}deg,
          #e9c46a ${(9 * 360 / 11)}deg, #e9c46a ${(10 * 360 / 11) - 1}deg,
          #2a9d8f ${(10 * 360 / 11)}deg, #2a9d8f 360deg
        )`,
                    }}
                >
                    {prizes.map((prize, index) => (
                        <div
                            key={index}
                            className="absolute text-center text-white font-bold"
                            style={{
                                transform: ` rotate(${index * (360 / prizes.length)}deg) translateX(9rem) rotate(-${index * (360 / prizes.length)}deg)`,
                            }}
                        >
                            {prize}
                        </div>
                    ))}
                </div>
                <div
                    className="absolute w-8 h-8 bg-yellow-500 border-2 border-gray-800 rounded-full top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2"></div>
            </div>

            <button
                onClick={() => {
                    spin()
                }}
                disabled={spinning}
                className="mt-8 px-6 py-3 bg-blue-500 text-white font-semibold rounded-lg shadow-lg hover:bg-blue-600 transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
                {spinning ? getTranslatedText('spinning') : getTranslatedText('spinWheel')}
            </button>

            {selectedPrize !== null && (
                <div
                    className="absolute w-full h-full bg-black bg-opacity-80 flex justify-center items-center font-bold">
                    <p className={"text-2xl text-blue-500"}>
                        {getTranslatedText('congratulations')}
                        <span className="text-4xl animate-pulse text-yellow"> {selectedPrize} </span>
                        {getTranslatedText('coins')}
                    </p>
                </div>
            )}
        </div>
    );
};

export default WheelOfFortune;
