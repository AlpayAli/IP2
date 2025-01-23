INSERT INTO deck (id)
VALUES ('00000000-0000-0000-0000-000000000002');

INSERT INTO player (id, username, xp, balance, email, is_mail_verified, is_daily_spin_available)
VALUES ('22222222-2222-2222-2222-222222222222', 'Player1', 100, 500.0, 'player@example.com', true, true),
       ('33333333-3333-3333-3333-333333333333', 'Player2', 200, 600.0, 'user@example.com', true, true),
       ('56786660-b781-42e3-9dd9-a7b6dfdf706f', 'Admin', 200, 600.0, 'admin@example.com', true, true)
        ,
       ('123e4567-e89b-12d3-a456-426614174000', 'Host Player', 200, 10000.0, 'host@example.com', true, true)
        ,
       ('223e4567-e89b-12d3-a456-426614174111', 'Invited Player 1', 400, 500000.0, 'invited1@example.com', true, true)
        ,
       ('323e4567-e89b-12d3-a456-426614174222', 'Invited Player 2', 600, 666660.0, 'invited2@example.com', true, true)
        ,
       ('66c8d93b-6009-4b2b-a8d2-e57e0c909fa1', 'Keycloak Test user', 700, 80000.0, 'test@user.com', true, true),
       ('afdeffb7-4132-4293-a9ca-49e1524a2976', 'User', 700, 999990.0, 'test@user.com', true, true),
       ('4f70c7ff-46c4-4913-bb9c-392a621a1b00', 'PlayerToInvite', 700, 99990.0, 'test2@user.com', true, true);

INSERT INTO player (id, username, xp, balance, email, is_mail_verified, is_daily_spin_available, avatar_url)
VALUES ('23222222-4522-2222-2222-672222222222', 'ProfilePlayer', 100, 500.0, 'profilePlayer@example.com', true, true, 'https://storage.googleapis.com/image_bucket_ip2/general/winter-find-game.png');


INSERT INTO game (id, is_open, min_players, max_players, small_blind, host_id, game_status, buy_in_amount)
VALUES ('11111111-1111-1111-1111-111111111111', true, 2, 9, 50, NULL,
        'WAITING_FOR_PLAYERS', 5000);




INSERT INTO round (id, game_id, status, deck_id, pot)
VALUES ('00000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'PRE_FLOP',
        '00000000-0000-0000-0000-000000000002', 500);



INSERT INTO deck_cards (deck_id, cards)
VALUES ('00000000-0000-0000-0000-000000000002', 'TWO_OF_CLUBS'),
       ('00000000-0000-0000-0000-000000000002', 'THREE_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'FOUR_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'FIVE_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'SIX_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'SEVEN_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'EIGHT_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'NINE_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'TEN_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'JACK_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'QUEEN_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'KING_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'ACE_OF_CLUBS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'TWO_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'THREE_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'FOUR_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'FIVE_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'SIX_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'SEVEN_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'EIGHT_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'NINE_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'TEN_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'JACK_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'QUEEN_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'KING_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'ACE_OF_DIAMONDS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'TWO_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'THREE_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'FOUR_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'FIVE_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'SIX_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'SEVEN_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'EIGHT_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'NINE_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'TEN_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'JACK_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'QUEEN_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'KING_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'ACE_OF_HEARTS')
        ,
       ('00000000-0000-0000-0000-000000000002', 'TWO_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'THREE_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'FOUR_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'FIVE_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'SIX_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'SEVEN_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'EIGHT_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'NINE_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'TEN_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'JACK_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'QUEEN_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'KING_OF_SPADES')
        ,
       ('00000000-0000-0000-0000-000000000002', 'ACE_OF_SPADES');

INSERT INTO achievement (achievement_id, achievement_name, achievement_description, achievement_xp, hand_rank)
VALUES
    (gen_random_uuid(), 'Win with a Royal Flush', 'Win a round with a Royal Flush', 500, 'ROYAL_FLUSH'),
    (gen_random_uuid(), 'Win with a Straight Flush', 'Win a round with a Straight Flush', 400, 'STRAIGHT_FLUSH'),
    (gen_random_uuid(), 'Win with Four of a Kind', 'Win a round with Four of a Kind', 300, 'FOUR_OF_A_KIND'),
    (gen_random_uuid(), 'Win with a Full House', 'Win a round with a Full House', 250, 'FULL_HOUSE'),
    (gen_random_uuid(), 'Win with a Flush', 'Win a round with a Flush', 200, 'FLUSH'),
    (gen_random_uuid(), 'Win with a Straight', 'Win a round with a Straight', 150, 'STRAIGHT'),
    (gen_random_uuid(), 'Win with Three of a Kind', 'Win a round with Three of a Kind', 100, 'THREE_OF_A_KIND'),
    (gen_random_uuid(), 'Win with Two Pair', 'Win a round with Two Pair', 50, 'TWO_PAIR'),
    (gen_random_uuid(), 'Win with One Pair', 'Win a round with One Pair', 25, 'ONE_PAIR'),
    (gen_random_uuid(), 'Win with High Card', 'Win a round with a High Card', 10, 'HIGH_CARD');


INSERT INTO gimmick(id, name, xp_cost,balance_cost, image_url)
VALUES
    (gen_random_uuid(),'Table background 2', 50, 100,'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/2.png'),
    (gen_random_uuid(),'Table background 3', 150,150, 'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/3.png'),
    (gen_random_uuid(),'Table background 4', 250,6000, 'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/4.png'),
    (gen_random_uuid(),'Table background 5', 350,6500, 'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/5.png'),
    (gen_random_uuid(),'Table background 6', 450,7000, 'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/6.png'),
    (gen_random_uuid(),'Table background 7', 550,7500, 'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/7.png'),
    (gen_random_uuid(),'Table background 8', 650,8000, 'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/8.png'),
    (gen_random_uuid(),'Table background 9', 750, 8500,'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/9.png'),
    (gen_random_uuid(),'Table background 10', 850, 9000,'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/10.png'),
    (gen_random_uuid(),'Table background 11', 950, 9500,'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/11.png'),
    (gen_random_uuid(),'Table background 12', 1000, 10000,'https://storage.googleapis.com/image_bucket_ip2/general/table-bg/12.png');




