import {useEffect, useState} from "react";

export function ProgressBar(props: {duration: number, onComplete?: () => void}) {
    const [progress, setProgress] = useState(0); // Start met 0%

    useEffect(() => {
        const interval = 100; // Update elke 100ms
        const step = 100 / (props.duration * 10); // Hoeveelheid progressie per interval

        const timer = setInterval(() => {
            setProgress((prev) => {
                if (prev + step >= 100) {
                    clearInterval(timer); // Stop de timer als 100% is bereikt
                    if (props.onComplete) {
                        props.onComplete(); // Roep onComplete aan
                    }
                    return 100;
                }
                return prev + step;
            });
        }, interval);

        return () => clearInterval(timer); // Opruimen bij unmount
    }, [props.duration, props.onComplete]);

    return (
        <div className="w-full bg-gray-300 rounded-md overflow-hidden h-6">
            <div
                className={`h-full ${progress < 60 && "bg-blue-500"} ${progress > 70 && "bg-red"} ${
                    progress === 100 && "animate-ping"
                } transition-all duration-100`}
                style={{ width: `${progress}%` }}
            ></div>
        </div>
    );
}

export default ProgressBar;
