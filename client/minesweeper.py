import os
import json
import requests

url = 'https://minesweeper-api-springboot.herokuapp.com/minesweeper'


def clear():
    os.system('clear')


def menu():
    clear()
    print('Ingrese la operacion: ')
    print('1 - Login')
    print('2 - Register')
    print('99 - Salir\n')


def login(username, password):
    data = {'username': username, 'password': password}
    json_data = json.dumps(data)
    headers = {'Content-type':'application/json', 'Accept':'application/json'}
    response = requests.post(url + '/auth/login', data=json_data,headers=headers)
    if response.status_code == 200:
        json_response = response.json()
        return True, json_response['authToken']
    else:
        print('Error... ')
        print(response.status_code)
    return False, ''


def send_post_request(auth_token, data):
    json_data = json.dumps(data)
    token = 'Bearer ' + auth_token
    headers = {'Content-type': 'application/json', 'Accept': 'application/json', 'Authorization': token}
    response = requests.post(url + '/game', data=json_data, headers=headers)
    if response.status_code == 200:
        return True, response.json()
    else:
        print('Error... ')
        print(response.status_code)
    return False, ''


def send_put_request(auth_token, data):
    json_data = json.dumps(data)
    token = 'Bearer ' + auth_token
    headers = {'Content-type': 'application/json', 'Accept': 'application/json', 'Authorization': token}
    response = requests.put(url + '/game', data=json_data, headers=headers)
    if response.status_code == 200:
        return True, response.json()
    else:
        print('Error... ')
        print(response.status_code)
    return False, ''


def send_get_request(auth_token):
    token = 'Bearer ' + auth_token
    headers = {'Content-type': 'application/json', 'Accept': 'application/json', 'Authorization': token}
    response = requests.get(url + '/game', headers=headers)
    if response.status_code == 200:
        return True, response.json()
    else:
        print('Error... ')
        print(response.status_code)
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
    _, json_response = send_put_request(auth_token, data)
    return json_response['board'], json_response['gameStatus'], json_response['gameId']


def get_shoot_type():
    shoot_choose = input('Discover or Flag cell? 1/2 >> ')
    if shoot_choose == 1:
        return 'DISCOVER'
    if shoot_choose == 2:
        return 'FLAG'


def play(board, game_status, game_id, auth_token):
    while game_status == 'PLAYING':
        clear()
        show_board(board)
        print('Enter nex move..')
        shoot_type = get_shoot_type()
        row = input('Row >> ')
        column = input('Column >> ')
        board, game_status, game_id = send_play(row, column, game_id, shoot_type, auth_token)
    clear()
    if game_status == 'GAME_OVE':
        print('GAME OVER!!! try again :)')
    if game_status == 'WIN':
        print('YOU WIN!!! Congrats!! :)')


def retrieve_old(auth_token):
    _, json_response = send_get_request(auth_token)
    print('ID - creation date - game status - time spend')
    i = 0
    for response in json_response:
        create_date = response['createdDate']
        game_status = response['gameStatus']
        time_spend = response['timeSpend']
        print('{} - {} {} {} '.format(i, create_date, game_status, time_spend))
        i += 1
    game_choose = input('Input game ID >> ')
    game = json_response[int(game_choose)]
    play(game['board'], game['gameStatus'], game['gameId'], auth_token)


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
    clear()
    print('Ingrese la operacion: ')
    print('1 - New game')
    print('2 - Resume old one')
    print('99 - Exit \n')
    game_option = input('Enter username >> ')
    process_game(game_option, auth_token)


def process_option(option):
    if option == '1':
        username = input('Enter username >> ')
        password = input('Enter password >> ')
        success, auth_token  = login(username, password)
        if success:
            user_screen(auth_token)


while True:
    menu()
    op = input('Choose from menu >> ')
    process_option(op)
