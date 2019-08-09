"use strict";
// vim: ts=2 sw=2 sts=2 et:
/*
 *=======================================================================
 *    Filename:index.js
 *
 *     Version: 1.0
 *  Created on: August 31, 2018
 *
 *      Author: corvo
 *=======================================================================
 */

let data={
  "name": "a good",
  "price": 21.0,
  "desc": "a good only use to test",
};

let data_name = data["name"];


document.getElementById("data-info").innerHTML = data_name;

$("#detail").on("click", function() {
  let data_pretty = JSON.stringify(data); // 序列化成字符串
  let info = btoa(data_pretty);
  window.location = `detail.html?info=${info}`;
});
