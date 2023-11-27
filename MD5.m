testStrings = {'siddharth', 'abinaya', 'sanjana'};
for i = 1:length(testStrings)
    s = testStrings{i};
    md5Hash = md5_digest(s);
    fprintf('MD5 Hash: %s for "%s"\n', md5Hash, s);
end

%%
function digest = md5_digest(message)
    % Constants
    m = 2 ^ 32;
    s = [-7, -12, -17, -22
         -5,  -9, -14, -20
         -4, -11, -16, -23
         -6, -10, -15, -21];
    t = floor(abs(sin(1:64)) .* m);
    digest = [hex2dec('67452301') ...
              hex2dec('EFCDAB89') ...
              hex2dec('98BADCFE') ...
              hex2dec('10325476')];

    % Convert message to bytes
    message = double(message);

    % the numel function is used to determine the number of elements in the variable message.
    bytelen = numel(message);

    % padding the message array, it appends 128 (binary representation of the bit 1 followed by seven 0 bits) to the message
    message = [message, 128, zeros(1, mod(55 - bytelen, 64))];

    % reshape the message array into a 2D array with 4 rows. 
    % The number of columns is equal to the total number of elements in message divided by 4.
    message = reshape(message, 4, numel(message) / 4);

    % combining each group of 4 bytes into a single 32-bit word
    message = message(1,:) + ...
              message(2,:) * 256 + ...
              message(3,:) * 65536 + ...
              message(4,:) * 16777216;
    
    % calculates the length of the original message in bits by multiplying the length in bytes by 8.
    bitlen = bytelen * 8;

    % appends the bit length of the original message to the end of the message array
    message = [message, mod(bitlen, m), mod(bitlen / m, m)];

    % Process each 512-bit block
    % iterate over the message array in chunks of 16 elements
    for k = 1:16:numel(message)

        % initialize the variables a, b, c, and d with the current values of the digest (initial condition are given above)
        a = digest(1); 
        b = digest(2); 
        c = digest(3); 
        d = digest(4);

        for i = (1:64)
            % convert the b , c, d to binary variable that is bv , cv, dv (bv -> binary variable)
            bv = dec2bin(b, 32) - '0';
            cv = dec2bin(c, 32) - '0';
            dv = dec2bin(d, 32) - '0';

            % list of operational logic is given here 
            if i <= 16
                f = (bv & cv) | (~bv & dv);
                ki = i - 1;
                sr = 1;

            elseif i <= 32
                f = (bv & dv) | (cv & ~dv);
                ki = mod(5 * i - 4, 16);
                sr = 2;

            elseif i <= 48
                f = xor(bv, xor(cv, dv));
                ki = mod(3 * i + 2, 16);
                sr = 3;

            else
                f = xor(cv, bv | ~dv);
                ki = mod(7 * i - 7, 16);
                sr = 4;
            end

            
            f = bin2dec(char(f + '0'));

            % calculate the circular shift number form that table 
            sc = mod(i - 1, 4) + 1;

            % calculates the sum of a, f, the current message word, and the current constant, modulo m.
            sum = mod(a + f + message(k + ki) + t(i), m);
            sum = dec2bin(sum, 32);

            % circular shift the sum by the number of bits given in the table
            sum = circshift(sum, [0, s(sr, sc)]);
            sum = bin2dec(sum);

            % the changing process happens here
            temp = d;
            d = c;
            c = b;
            b = mod(b + sum, m);
            a = temp;
        end

        % end of 512 bit block completeion (total 64 rounts has happened here)
        digest = mod(digest + [a, b, c, d], m);
    end

    % Convert hash to bytes and then to hexadecimal
    % division by powers of 256 is equivalent to a right shift of 8, 16, and 24 bits, respectively. 
    % This operation is used to split each 32-bit word in the digest into four 8-bit bytes
    % then we assemble it and return the final hash
    digest = [digest
              digest / 256
              digest / 65536
              digest / 16777216];
    digest = reshape(mod(floor(digest), 256), 1, numel(digest));
    digest = dec2hex(digest);
    digest = reshape(transpose(digest), 1, numel(digest));
    digest = lower(digest);
end