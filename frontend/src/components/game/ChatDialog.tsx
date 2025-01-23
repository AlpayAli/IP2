import { useState } from "react";
import { TextField, Button, Box, Typography, Dialog } from "@mui/material";
import ReportIcon from '@mui/icons-material/Report';
import { useMessages } from "../../hooks/useMessages.ts";
import { format } from "date-fns";

interface ChatProps {
    gameId: string;
    isOpen: boolean;
    onClose: () => void;
}

export function ChatDialog({ gameId, isOpen, onClose }: ChatProps) {
    const { messages, isLoadingMessages, isErrorLoadingMessages, refetch } = useMessages(gameId);
    const { sendMessage } = useMessages(gameId);
    const [newMessage, setNewMessage] = useState("");

    const handleSendMessage = () => {
        if (!newMessage.trim()) return;

        sendMessage(newMessage, {
            onSuccess: () => {
                setNewMessage("");
                refetch();
            },
            onError: (error) => {
                console.error("Failed to send message:", error);
            },
        });
    };

    if (isLoadingMessages) return <Typography>Loading messages...</Typography>;
    if (isErrorLoadingMessages) return <Typography>Error loading messages.</Typography>;

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            handleSendMessage();
        }
    };

    return (
        <Dialog open={isOpen} onClose={onClose} fullWidth>
            <Box p={2}>
                <Typography variant="h6" gutterBottom>
                    Game Chat
                </Typography>
                <Box
                    sx={{
                        height: "250px",
                        overflowY: "auto",
                        paddingRight: "8px",
                        display: "flex",
                        flexDirection: "column-reverse"
                    }}
                >
                    <ul style={{ listStyleType: "none", padding: 0, fontFamily: "Arial, sans-serif" }}>
                        {messages?.map((message, index) => (
                            <li
                                key={index}
                                style={{
                                    display: "flex",
                                    alignItems: "center",
                                    marginBottom: "8px",
                                }}
                            >
                                <span style={{ color: "gray", marginRight: "8px", minWidth: "50px" }}>
                                    {formatDate(message.dateSend)}
                                </span>
                                <strong style={{ marginRight: "8px", flexShrink: 0 }}>
                                    {message.playerName}:
                                </strong>
                                <span>{decodeMessageContent(message.content)}</span>
                            </li>
                        ))}
                    </ul>
                </Box>
                <Box mt={2} display="flex">
                    <TextField
                        fullWidth
                        variant="outlined"
                        placeholder="Type a message"
                        value={newMessage}
                        onKeyDown={handleKeyDown}
                        onChange={(e) => setNewMessage(e.target.value)}
                    />
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleSendMessage}
                        style={{ marginLeft: "8px" }}
                    >
                        Send
                    </Button>
                </Box>
                <div
                    className={`flex items-center space-x-2 mt-2 ${
                        newMessage.length > 255 ? "visible" : "hidden"
                    }`}
                >
                    <ReportIcon className="text-red-600" />
                    <p className="text-sm text-red-600 font-medium">
                        Maximum message length is 255 characters.
                    </p>
                </div>
            </Box>
        </Dialog>
    );
}

function decodeMessageContent(content: string): string {
    return decodeURIComponent(content);
}

function formatDate(date: string): string {
    return format(new Date(date), "HH:mm");
}
