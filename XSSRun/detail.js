"use strict";
// vim: ts=2 sw=2 sts=2 et:
/*
 *=======================================================================
 *    Filename:detail.js
 *
 *     Version: 1.0
 *  Created on: August 31, 2018
 *
 *      Author: corvo
 *=======================================================================
 */

// 接受从其他页面来的请求参数
function getParams(key) {
  var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  return null;
}


let info = getParams("info");
let data_info = JSON.parse(atob(info));

console.log(data_info);

let data_pretty = JSON.stringify(data_info, null, 2);
document.getElementById("data-info").innerHTML = data_pretty;
