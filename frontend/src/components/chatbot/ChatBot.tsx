import { useChatBot } from "../../hooks/useChatBot.ts";
import {useContext, useEffect, useRef, useState} from "react";
import SettingsContext, {ISettingsContext} from "../../context/settings/SettingsContext.ts";

export function ChatBot() {
    const { sendMessage, status, isErrorLoadingChatBotResponse, chatBotResponse } = useChatBot();
    const [messages, setMessages] = useState<{ question: string; answer: string | null }[]>([]);
    const [userQuestion, setUserQuestion] = useState<string>('');
    const [isTyping, setIsTyping] = useState<boolean>(false);
    const messagesEndRef = useRef<HTMLDivElement | null>(null);
    const { language } = useContext<ISettingsContext>(SettingsContext);
    const getTranslatedText = (key: string): string => {
        switch (language) {
            case 'EN':
                if (key === 'chatBot') return 'ChatBot';
                if (key === 'typeYourQuestion') return 'Type your question...';
                if (key === 'sending') return 'Sending...';
                if (key === 'send') return 'Send';
                break;
            case 'NL':
                if (key === 'chatBot') return 'ChatBot';
                if (key === 'typeYourQuestion') return 'Typ je vraag...';
                if (key === 'sending') return 'Verzenden...';
                if (key === 'send') return 'Verstuur';
                break;
            case 'FR':
                if (key === 'chatBot') return 'ChatBot';
                if (key === 'typeYourQuestion') return 'Tapez votre question...';
                if (key === 'sending') return 'Envoi...';
                if (key === 'send') return 'Envoyer';
                break;
            default:
                if (key === 'chatBot') return 'ChatBot';
                if (key === 'typeYourQuestion') return 'Type your question...';
                if (key === 'sending') return 'Sending...';
                if (key === 'send') return 'Send';
                break;
        }
        return '';
    };
    const handleAsk = async () => {
        if (!userQuestion.trim() || status === 'pending') return;
        setMessages((prevMessages) => [
            ...prevMessages,
            { question: userQuestion, answer: null },
        ]);
        try {
            await sendMessage(userQuestion);
            setUserQuestion('');
        } catch (error) {
            console.error('Error asking chatbot:', error);
            setUserQuestion('');
        }
    };

    useEffect(() => {
        if (chatBotResponse) {
            setIsTyping(true);
            setMessages((prevMessages) => {
                const updatedMessages = [...prevMessages];
                const lastMessage = updatedMessages[updatedMessages.length - 1];
                if (lastMessage && lastMessage.answer === null) {
                    lastMessage.answer = chatBotResponse;
                }
                return updatedMessages;
            });
        }
    }, [chatBotResponse]);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            handleAsk();
        }
    };

    return (
        <div className="bg-white w-full max-w-md p-6 rounded-lg shadow-md">
            <h1 className="text-2xl font-bold mb-4 text-gray-800">ChatBot</h1>
            <div className="flex flex-col space-y-4 h-[400px] overflow-y-auto border border-gray-300 rounded p-4 bg-gray-50">
                {messages.map((message, index) => (
                    <div key={index} className="space-y-2">
                        <Question question={message.question} />
                        <Answer
                            answer={message.answer ?? "ChatBot is momenteel niet beschikbaar."}
                            setIsTyping={setIsTyping}
                            isError={isErrorLoadingChatBotResponse}
                        />
                    </div>
                ))}
                <div ref={messagesEndRef} />
            </div>
            <div className="mt-4 flex">
                <input
                    type="text"
                    placeholder={getTranslatedText('typeYourQuestion')}
                    value={userQuestion}
                    onChange={(e) => setUserQuestion(e.target.value)}
                    className="flex-1 p-2 border border-gray-300 rounded-l focus:outline-none focus:ring-2 focus:ring-blue-400"
                    onKeyDown={handleKeyDown}
                    disabled={isTyping || status === 'pending'}
                />
                <button
                    onClick={handleAsk}
                    disabled={isTyping || status === 'pending'}
                    className={`p-2 bg-blue-500 text-white rounded-r ${
                        isTyping || status === 'pending' ? 'opacity-50' : 'hover:bg-blue-600'
                    }`}
                >
                    {status === 'pending' ? getTranslatedText('sending') : getTranslatedText('send')}
                </button>
            </div>
        </div>
    );
}

function Answer({
                    answer,
                    setIsTyping,
                    isError,
                }: {
    answer: string;
    setIsTyping: (isTyping: boolean) => void;
    isError: boolean;
}) {
    const [animatedAnswer, setAnimatedAnswer] = useState('');
    const finalAnswer = isError ? 'ChatBot is momenteel niet beschikbaar.' : answer;

    useEffect(() => {
        if (!finalAnswer) return;

        setIsTyping(true);
        let currentIndex = 0;
        setAnimatedAnswer('');

        const typingInterval = setInterval(() => {
            setAnimatedAnswer(finalAnswer.substring(0, currentIndex + 1));
            currentIndex++;

            if (currentIndex === finalAnswer.length) {
                clearInterval(typingInterval);
                setIsTyping(false);
            }
        }, 50);

        return () => clearInterval(typingInterval);
    }, [finalAnswer, setIsTyping]);

    return (
        <div
            className={`p-2 rounded-lg self-end max-w-xs ${
                isError ? 'bg-red-100 text-red-800' : 'bg-blue-100 text-blue-800'
            }`}
        >
            <span className="text-sm">{animatedAnswer}</span>
        </div>
    );
}


function Question({ question }: { question: string }) {
    return (
        <div className="bg-gray-200 text-gray-800 p-2 rounded-lg self-start max-w-xs">
            <span className="text-sm">{question}</span>
        </div>
    );
}
