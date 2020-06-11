import os
import json
import requests

url = 'https://minesweeper-api-springboot.herokuapp.com/minesweeper'
uri_game = '/game'
uri_auth = '/auth'


def clear():
    os.system('clear')


def menu():

    print('Enter an option: ')
    print('1 - Login')
    print('2 - Register')
    print('99 - Exit\n')

def send_auth(username, password, uri):
    data = {'username': username, 'password': password}
    json_data = json.dumps(data)
    headers = {'Content-type':'application/json', 'Accept':'application/json'}
    response = requests.post(url + uri_auth + uri, data=json_data,headers=headers)
    json_response = response.json()
    if response.status_code == 200:
        return True, json_response['authToken']
    else:
        return False, json_response['message']


def login(username, password):
    return send_auth(username, password, '/login')


def register(username, password):
    return send_auth(username, password, '/register')


def send_post_request(auth_token, data):
    json_data = json.dumps(data)
    token = 'Bearer ' + auth_token
    headers = {'Content-type': 'application/json', 'Accept': 'application/json', 'Authorization': token}
    response = requests.post(url + uri_game, data=json_data, headers=headers)
    if response.status_code == 200:
        return True, response.json()
    else:
        return False, ''


def send_put_request(auth_token, data):
    json_data = json.dumps(data)
    token = 'Bearer ' + auth_token
    headers = {'Content-type': 'application/json', 'Accept': 'application/json', 'Authorization': token}
    response = requests.put(url + uri_game, data=json_data, headers=headers)
    if response.status_code == 200:
        response_json = response.json()
        return True, response_json['board'], response_json['gameStatus'], response_json['gameId']
    else:
        return False, '', '', ''


def send_get_request(auth_token):
    token = 'Bearer ' + auth_token
    headers = {'Content-type': 'application/json', 'Accept': 'application/json', 'Authorization': token}
    response = requests.get(url + uri_game, headers=headers)
    if response.status_code == 200:
        return True, response.json()
    else:
        return False, ''


def new_game(row, column, bombs, auth_token):
    data = {'bombs': bombs, 'column': column, 'row': row}
    _, json_response = send_post_request(auth_token, data)
    return json_response['board'], json_response['gameStatus'], json_response['gameId']


def show_board(board):
    print(' ', end='')
    for x in range(len(board)):
        print(' ' + str(x), end='')
    print('')
    i = 0
    for row in board:
        print(str(i) + ' ', end='')
        i += 1
        for number in row:
            if number == 0:
                print('  ', end='')
            if number == -2:
                print('0 ', end='')
            if number == -3:
                print('? ', end='')
            if number == -1:
                print('X ', end='')
            if number > 0:
                print(str(number) + ' ', end='')
        print('')


def send_play(row, column, game_id, shoot_type, auth_token):
    data = {'column': column, 'gameId': game_id, 'row': row, 'shootType': shoot_type}
    _, board, game_status, game_id = send_put_request(auth_token, data)
    return board, game_status, game_id


def get_shoot_type():
    shoot_choose = input('Discover(1) or Flag(2) cell or EXIT(99)? 1/2/99 >> ')
    if shoot_choose == '1':
        return 'DISCOVER'
    if shoot_choose == '2':
        return 'FLAG'
    if shoot_choose == '99':
        return 'EXIT'


def play(board, game_status, game_id, auth_token):
    while game_status == 'PLAYING':
        clear()
        show_board(board)
        print('Enter nex move..')
        shoot_type = get_shoot_type()
        if shoot_type == 'EXIT':
            break
        row = input('Row >> ')
        column = input('Column >> ')
        board, game_status, game_id = send_play(int(row), int(column), int(game_id), shoot_type, auth_token)
    clear()
    if game_status == 'EXIT':
        return
    if game_status == 'GAME_OVER':
        print('GAME OVER!!! try again :)')
    if game_status == 'WIN':
        print('YOU WIN!!! Congrats!! :)')
    print('')
    show_board(board)
    input('\n\nPress a key to continue...')


def retrieve_old(auth_token):
    _, json_response = send_get_request(auth_token)
    clear()
    print('===============================================')
    print('ID - creation date - game status - time spend')
    print('===============================================')
    i = 0
    for response in json_response:
        create_date = response['createdDate']
        game_status = response['gameStatus']
        time_spend = response['timeSpend']
        print('{} - date: {} status: {} time spend: {} seconds '.format(i, create_date, game_status, time_spend))
        i += 1
    game_choose = input('\n\nID >> ')
    game = json_response[int(game_choose)]
    play(game['board'], game['gameStatus'], int(game['gameId']), auth_token)


def process_game(game_option, auth_token):
    if game_option == '1':
        clear()
        print('Set up board game..')
        row = input('input rows >> ')
        column = input('input columns >> ')
        bombs = input('input bombs >> ')
        board, game_status, game_id = new_game(row, column, bombs, auth_token)
        play(board, game_status, game_id, auth_token)
    if game_option == '2':
        retrieve_old(auth_token)


def user_screen(auth_token):
    game_option = ''
    while game_option != '99':
        clear()
        print('Enter an option: ')
        print('1 - New game')
        print('2 - Resume old one')
        print('99 - back \n')
        game_option = input('Option >> ')
        process_game(game_option, auth_token)


def process_option(option):
    username = input('Enter username >> ')
    password = input('Enter password >> ')
    if option == '1':
        success, response  = login(username, password)
    if option == '2':
        success, response  = register(username, password)
    if success:
        user_screen(response)
    else:
        clear()
        print(response)
        input('\n\nPress a key to continue...')

while True:
    clear()
    print(' ___  ___ _               _____                                          ')
    print(' |  \/  |(_)             /  ___|                                         ')
    print(' | .  . | _  _ __    ___ \ `--. __      __  ___   ___  _ __    ___  _ __ ')
    print(' | |\/| || || \'_ \  / _ \ `--. \\ \ /\ / / / _ \ / _ \| \'_ \  / _ \| \'__|')
    print(' | |  | || || | | ||  __//\__/ / \ V  V / |  __/|  __/| |_) ||  __/| |   ')
    print(' \_|  |_/|_||_| |_| \___|\____/   \_/\_/   \___| \___|| .__/  \___||_|   ')
    print('                                                     | |                ')
    print('                                                     |_|                ')
    menu()
    op = input('Choose from menu >> ')
    process_option(op)
