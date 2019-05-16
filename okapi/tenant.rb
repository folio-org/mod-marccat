#!/usr/bin/env ruby

ruby -rjson -e 'j = JSON.parse(File.read("tenant.json")); puts j[0]["tenant"]'
