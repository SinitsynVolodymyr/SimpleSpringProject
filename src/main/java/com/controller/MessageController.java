package com.controller;

import com.exception.PageNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("message")
public class MessageController {
    public int idCounter = 3;

    public List<Map<String,String>> massages = new ArrayList<Map<String,String>>(){{
        add(new HashMap<String,String>() {{put("id","1"); put("text", "First massage");}});
        add(new HashMap<String,String>() {{put("id","2"); put("text", "Second massage");}});
        add(new HashMap<String,String>() {{put("id","3"); put("text", "Third massage");}});
    }};

    @GetMapping
    public List<Map<String,String>> list(){
        return massages;
    }

    @GetMapping("{id}")
    public Map<String,String> getOne(@PathVariable String id){
        return getMassage(id);
    }

    private Map<String, String> getMassage(String id) {
        return massages.stream()
                .filter(massage -> massage.get("id").equals(id))
                .findFirst()
                .orElseThrow(PageNotFoundException::new);
    }

    @PostMapping
    public Map<String,String> create(@RequestBody Map<String,String> mess){
        HashMap<String, String> result = new HashMap<>();
        result.put("id","-1");
        result.putAll(mess);
        result.put("id",String.valueOf(++idCounter));
        massages.add(result);
        return result;
    }

    @PutMapping()
    public Map<String,String> update(@RequestBody Map<String,String> massage){
        String id = massage.get("id");
        if (id==null) throw new PageNotFoundException();
        Map<String, String> massageFromDb = getMassage(id);

        massageFromDb.putAll(massage);

        return massageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Map<String, String> massage = getMassage(id);
        massages.remove(massage);
    }

}
