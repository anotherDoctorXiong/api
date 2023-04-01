---限流策略,每个key流量监控24小时,达到上限返回0,未达到上限返回当前次数

local key = KEYS[1]   --限流key
local limitCount = tonumber(ARGV[1])       --限流大小
local limitTime = tonumber(ARGV[2])        --限流时间
local current = redis.call('get', key);
if current then
    if current + 1 > limitCount then --如果超出限流大小
        return 0
    else
        redis.call("INCRBY", key,"1")
        return current + 1
    end
else
    redis.call("set", key,"1")
    redis.call("expire", key,limitTime)
    return 1
end


