import { describe,expect, test, vi } from "vitest";
import { playSound } from "./SoundUtils.ts";

describe("playSound", () => {
    test("should create an Audio instance and play the sound with correct volume", () => {
        // Mock the Audio constructor
        const mockPlay = vi.fn();
        const mockAudioConstructor = vi.fn(() => ({
            play: mockPlay,
            volume: 0,
        }));

        // Replace global Audio with mock
        globalThis.Audio = mockAudioConstructor as unknown as typeof Audio;

        // Input values
        const soundUrl = "https://example.com/sound.mp3";
        const volume = 50;

        // Call the function
        playSound(soundUrl, volume);

        // Assertions
        expect(mockAudioConstructor).toHaveBeenCalledWith(soundUrl);
        expect(mockAudioConstructor.mock.results[0].value.volume).toBe(0.5); // 50 / 100
        expect(mockPlay).toHaveBeenCalled();
    });

    test("should handle 0 volume correctly", () => {
        const mockPlay = vi.fn();
        const mockAudioConstructor = vi.fn(() => ({
            play: mockPlay,
            volume: 0,
        }));

        globalThis.Audio = mockAudioConstructor as unknown as typeof Audio;

        const soundUrl = "https://example.com/sound.mp3";
        const volume = 0;

        playSound(soundUrl, volume);

        expect(mockAudioConstructor).toHaveBeenCalledWith(soundUrl);
        expect(mockAudioConstructor.mock.results[0].value.volume).toBe(0); // 0 / 100
        expect(mockPlay).toHaveBeenCalled();
    });

    test("should handle 100 volume correctly", () => {
        const mockPlay = vi.fn();
        const mockAudioConstructor = vi.fn(() => ({
            play: mockPlay,
            volume: 0,
        }));

        globalThis.Audio = mockAudioConstructor as unknown as typeof Audio;

        const soundUrl = "https://example.com/sound.mp3";
        const volume = 100;

        playSound(soundUrl, volume);

        expect(mockAudioConstructor).toHaveBeenCalledWith(soundUrl);
        expect(mockAudioConstructor.mock.results[0].value.volume).toBe(1); // 100 / 100
        expect(mockPlay).toHaveBeenCalled();
    });
});
