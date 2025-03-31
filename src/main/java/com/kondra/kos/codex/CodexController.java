package com.kondra.kos.codex;

import com.tccc.kos.commons.core.context.annotations.Autowired;
import com.tccc.kos.commons.core.dispatcher.annotations.ApiController;
import com.tccc.kos.commons.core.dispatcher.annotations.ApiEndpoint;
import com.tccc.kos.commons.core.dispatcher.annotations.PathVariable;
import com.tccc.kos.commons.core.dispatcher.annotations.RequestBody;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@ApiController(base = "/codex", title = "Basic Codex Controller")
@Slf4j
public class CodexController {

    @Autowired
    CodexBasicService codexBasicService;

    /**
     *
     * @return endpoint that return a list of objects.
     */
    @ApiEndpoint(GET = "/list",desc = "Return the list of objects.")
    public Collection<TestObject> list() {
        return codexBasicService.getTestObjects();
    }

    /**
     *
     * @param testObject Test Object to be Added
     */
    @ApiEndpoint(POST = "/add", desc = "Add and item to the list")
    public void add(@RequestBody TestObject testObject) {
        codexBasicService.addObject(testObject);

    }

    /**
     *
     * @param testObject Modified Test Object
     */
    @ApiEndpoint(PUT = "/modify", desc = "Modify an item from the list")
    public void modify(@RequestBody TestObject testObject) {
        codexBasicService.modifyObject(testObject);
    }

    /**
     *
     * @param id of item to be removed from the list
     */
    @ApiEndpoint(DELETE = "/remove/{id}", desc = "remove an item from the list",
            params = @ApiEndpoint.Param(name = "id", desc = "The object id to remove"))
    public void remove(@PathVariable("id")int id) {
        codexBasicService.removeObject(id);
    }

}
