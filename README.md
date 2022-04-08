# Socket programming with Checksum

Improtant piece of code that calculates checksum is:
```java
public byte calculateCheckSum(byte[] dataInBytes) {
        int sum = 0;
        for (int i = 0; i < dataInBytes.length; i++) {
            sum += (dataInBytes[i]);
            int carry = (sum >> 7) & 1;
            if (carry == 1) {
                sum = sum & 127;
                sum += 1;
            }
        }

        byte checksum = (byte) (sum ^ 127);
        return checksum;
    }
    
```
