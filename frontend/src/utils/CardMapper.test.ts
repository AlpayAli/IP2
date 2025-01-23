import { describe, test, expect } from "vitest";
import { formatCardUrl } from "./CardMapper.ts";

describe("formatCardUrl", () => {
    test("formats card URL correctly", () => {
        // Test cases
        const cases = [
            { input: "ace_of_spades", expected: "https://storage.googleapis.com/image_bucket_ip2/playingcards-spades/1.png" },
            { input: "queen_of_hearts", expected: "https://storage.googleapis.com/image_bucket_ip2/playingcards-hearts/12.png" },
            { input: "ten_of_diamonds", expected: "https://storage.googleapis.com/image_bucket_ip2/playingcards-diamonds/10.png" },
            { input: "king_of_clubs", expected: "https://storage.googleapis.com/image_bucket_ip2/playingcards-clubs/13.png" },
            { input: "three_of_spades", expected: "https://storage.googleapis.com/image_bucket_ip2/playingcards-spades/3.png" },
        ];

        // Execute tests
        cases.forEach(({ input, expected }) => {
            expect(formatCardUrl(input)).toBe(expected);
        });
    });

    test("handles unknown cards gracefully", () => {
        const invalidInput = "joker_of_anything";
        const expectedUrl =
            "https://storage.googleapis.com/image_bucket_ip2/playingcards-anything/joker.png"; // Assumes fallback for unknown cards

        expect(formatCardUrl(invalidInput)).toBe(expectedUrl);
    });
});
