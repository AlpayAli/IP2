
export function playSound (sound: string, volume: number) {
    const audio = new Audio(sound)
    audio.volume = volume / 100
    audio.play()
}