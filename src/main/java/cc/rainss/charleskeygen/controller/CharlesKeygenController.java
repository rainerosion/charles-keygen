package cc.rainss.charleskeygen.controller;

import cc.rainss.charleskeygen.utils.GenerateSerialNumber;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rainerosion
 */
@RestController
public class CharlesKeygenController {

    @GetMapping(value = "/getSerialNumber")
    public Map<String,String> getSerialNumber(String name){
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        GenerateSerialNumber generateSerialNumber = new GenerateSerialNumber();
        String s = generateSerialNumber.calculateSerial(name);
        stringStringHashMap.put("code","200");
        stringStringHashMap.put("name",name);
        stringStringHashMap.put("serial_number",s);
        return stringStringHashMap;
    }

}
