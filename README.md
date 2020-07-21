###A peer to peer network setup for file sharing.
A central server (in this case firebase server) controls / manages the operations.

initially the central_list (files)  is empty , any device 'x' connected to the network can add file 'f' to the central_list

    By adding file we mean the central server gets notified that this device 'x' is ready/ willing to share this file 'f'.


*after opening the application the user is presented with a list of files available in the network in a card view format.
*Each card view has option to download this specific file , upon clicking on it starts the process of downloading indicating with a progress bar.
*there is a search bar to search for required files in the central_list/network using name of the file.


## So how is the process of transfer is done?


![request_to_central_server](https://github.com/khalil192/synerzy-complete/blob/master/images/request_to_central_server.png)

![actual_file_request_transfer_part](https://github.com/khalil192/synerzy-complete/blob/master/images/actual_file_request_transfer_part.png)

![acknowledgement_and_update_part](https://github.com/khalil192/synerzy-complete/blob/master/images/acknowledgement_and_update_part.png)
