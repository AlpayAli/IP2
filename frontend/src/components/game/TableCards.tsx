import {formatCardUrl} from "../../utils/CardMapper.ts";

interface ITableCards {
    tableCards: string[];
}

export function TableCards({tableCards}: ITableCards) {
    const cards: string[][] = [
        ["0", ],
        ["1", ],
        ["2", ],
        ["3", ],
        ["4", ],
    ];

    if (tableCards){
        for (let i = 0; i < cards.length; i++) {
            cards[i][1] = tableCards[i];
        }
    }

    return (
        <div
            className={"grid grid-cols-5 mx-auto h-max my-auto border-r-2 border-l-2 border-white border-opacity-50 w-1/2"}>
            {cards.map((card) => (
                <div key={card[0]}
                     className={"border-4 border-r-2 border-l-2 border-white border-opacity-50 h-max "}>

                    {!card[1] ?
                        <img
                            className={"p-1 h-full object-contain "}
                            src={"https://storage.googleapis.com/image_bucket_ip2/general/backcard.png"}
                            alt={"backsidecard"}>

                        </img> :
                        <img
                            className={"p-1 h-full object-contain "}
                            alt={card[1]}
                            src={formatCardUrl(card[1])}></img>
                    }
                </div>
            ))
            }

        </div>
    )
}