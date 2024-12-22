wrk.body = '{"SourceAccount":"3da6111a917b4aa09a0cce02e133c85a","DestAccount":"c684fa083ca14fe8ad8332b1f1d7c4aa","Amount":3}'

wrk.headers["Content-Type"] = "application/json"

response = function(status, headers, body)
    print(body)
end