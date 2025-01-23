export type Player = {
    id: string
    isActive : boolean
    isDailySpinAvailable : boolean
    name: string
    email: string
    balance: number
    currentCards: string[]
    avatarUrl: string
    xp: number
    selectedBackground?: string; // New property
}