local sourceAccount = KEYS[1]
local destAccount = KEYS[2]
local amount = tonumber(KEYS[3])
local sourceBalance = tonumber(redis.call('get', sourceAccount))
local destBalance = tonumber(redis.call('get', destAccount))
if sourceBalance and sourceBalance >= amount then
    if redis.call('set', sourceAccount, sourceBalance - amount) then
        if redis.call('set', destAccount, destBalance + amount) then
            return 1
        else
            redis.call('set', sourceAccount, sourceBalance)
            return 2
        end
    else
        return 3
    end
else
    return 4
end
