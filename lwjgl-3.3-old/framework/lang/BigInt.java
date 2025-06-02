package framework.lang;


public class BigInt
{
    private CharSequence val;
    public static final int UNSIGNED = 0;
    public static final int SIGNED = 1;
    public int sign;

    public BigInt()
    {
        val = "0";
        sign = UNSIGNED;
    }

    public BigInt(int i)
    {
        val = "" + i;
        sign = UNSIGNED;
    }

    public void setSign(int sign)
    {
        if(sign == UNSIGNED || sign == SIGNED)
            this.sign = sign;
        else throw new IllegalArgumentException("sign must be UNSIGNED or SIGNED");
    }

    public BigInt add(int value)
    {
        CharSequence cs = "" + value;
        return add(cs);
    }

    public BigInt sub(int value)
    {
        CharSequence cs = "" + value;
        return sub(cs);
    }

    public BigInt mul(int value)
    {
        CharSequence cs = "" + value;
        return mul(cs);
    }

    public BigInt div(int value)
    {
        CharSequence cs = "" + value;
        return div(cs);
    }

    public BigInt add(CharSequence str)
    {
        StringBuilder result = new StringBuilder();

        CharSequence valRev = new StringBuilder(this.val).reverse().toString();
        CharSequence strRev = new StringBuilder(str).reverse().toString();

        int maxLen = java.lang.Math.max(valRev.length(), strRev.length());
        valRev = padWithZeros(valRev, maxLen);
        strRev = padWithZeros(strRev, maxLen);

        int carry = 0;

        for (int i = 0; i < maxLen; i++) {
            int digit1 = valRev.charAt(i) - '0';
            int digit2 = strRev.charAt(i) - '0';

            int sum = digit1 + digit2 + carry;
            carry = sum / 10;
            result.append(sum % 10);
        }

        if (carry > 0) {
            result.append(carry);
        }
        this.val = result.reverse().toString();
        return this;
    }

    public BigInt sub(CharSequence str) {
        StringBuilder result = new StringBuilder();

        CharSequence valRev = new StringBuilder(this.val).reverse().toString();
        CharSequence strRev = new StringBuilder(str).reverse().toString();

        int maxLen = java.lang.Math.max(valRev.length(), strRev.length());
        valRev = padWithZeros(valRev, maxLen);
        strRev = padWithZeros(strRev, maxLen);

        int borrow = 0;

        for (int i = 0; i < maxLen; i++) {
            int digit1 = valRev.charAt(i) - '0' - borrow;
            int digit2 = strRev.charAt(i) - '0';

            if (digit1 < digit2) {
                digit1 += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }

            int diff = digit1 - digit2;
            result.append(diff);
        }

        // Remove leading zeros from the result
        while (result.length() > 1 && result.charAt(result.length() - 1) == '0') {
            result.setLength(result.length() - 1);
        }

        this.val = result.reverse().toString();
        return this;
    }

    public BigInt mul(CharSequence str) {
        int len1 = this.val.length();
        int len2 = str.length();
        int[] resultArr = new int[len1 + len2];

        for (int i = len1 - 1; i >= 0; i--) {
            int digit1 = this.val.charAt(i) - '0';
            for (int j = len2 - 1; j >= 0; j--) {
                int digit2 = str.charAt(j) - '0';
                int mul = digit1 * digit2 + resultArr[i + j + 1];

                resultArr[i + j + 1] = mul % 10;  // Store unit place
                resultArr[i + j] += mul / 10;     // Add carry to the next index
            }
        }

        StringBuilder result = new StringBuilder();
        for (int num : resultArr) {
            if (!(result.isEmpty() && num == 0)) {  // Skip leading zeros
                result.append(num);
            }
        }

        this.val = result.isEmpty() ? "0" : result.toString();
        return this;
    }

    public BigInt div(CharSequence divisor) {
        StringBuilder result = new StringBuilder();
        StringBuilder dividendPart = new StringBuilder("0");

        for (int i = 0; i < this.val.length(); i++) {
            dividendPart.append(this.val.charAt(i));

            while (dividendPart.length() > 1 && dividendPart.charAt(0) == '0') {
                dividendPart.deleteCharAt(0);
            }
            int count = 0;
            while (compare(dividendPart.toString(), divisor.toString()) >= 0) {
                dividendPart = new StringBuilder(subHelper(dividendPart.toString(), divisor.toString()));
                count++;
            }
            result.append(count);
        }

        // Remove leading zeros in result
        while (result.length() > 1 && result.charAt(0) == '0') {
            result.deleteCharAt(0);
        }

        this.val = result.toString();
        return this;
    }

    // Helper function for comparing two CharSequences
    private int compare(CharSequence str1, CharSequence str2) {
        if (str1.length() != str2.length()) {
            return str1.length() - str2.length();
        }
        return str1.toString().compareTo(str2.toString());
    }

    private CharSequence subHelper(CharSequence dividendPart, CharSequence divisor) {
        StringBuilder result = new StringBuilder();
        int borrow = 0;

        for (int i = 0; i < dividendPart.length(); i++) {
            int digit1 = dividendPart.charAt(i) - '0' - borrow;
            int digit2 = i < divisor.length() ? divisor.charAt(i) - '0' : 0;

            if (digit1 < digit2) {
                digit1 += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }
            result.append(digit1 - digit2);
        }

        // Remove leading zeros from the result
        while (result.length() > 1 && result.charAt(0) == '0') {
            result.deleteCharAt(0);
        }

        return result.toString();
    }

    private CharSequence padWithZeros(CharSequence str, int length) {
        StringBuilder strBuilder = new StringBuilder(str);
        while(strBuilder.length() < length)
            strBuilder.append("0");
        str = strBuilder.toString();
        return str;
    }

    public static BigInt pow2(int look)
    {
        BigInt result = new BigInt(2);
        for(int i = 0; i < look; i++)
            result.mul(2);
        return result;
    }

    @Override
    public String toString()
    {
        return val.toString();
    }
}
