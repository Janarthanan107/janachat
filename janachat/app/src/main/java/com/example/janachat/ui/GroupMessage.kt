package com.example.janachat.ui

class GroupMessage (val group_id : String?, val message_txt : String?, val time_stamp : Long?) {
    constructor() : this("", "", 0)
} // done