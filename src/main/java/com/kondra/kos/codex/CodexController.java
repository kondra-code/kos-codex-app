package com.kondra.kos.codex;

import com.tccc.kos.commons.core.context.annotations.Autowired;
import com.tccc.kos.commons.core.dispatcher.annotations.ApiController;
import com.tccc.kos.commons.core.dispatcher.annotations.ApiEndpoint;
import com.tccc.kos.commons.core.dispatcher.annotations.PathVariable;
import com.tccc.kos.commons.util.concurrent.future.FutureWork;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@ApiController(base = "/codex", title = "Basic Codex Controller")
@Slf4j
public class CodexController {

    @Autowired
    CodexBasicService codexBasicService;

    /**
     * @return endpoint that return a list of objects.
     */
    @ApiEndpoint(GET = "/objects", desc = "Return the list of objects.")
    public Collection<TestObject> getObjects() {
        return codexBasicService.getTestObjects();
    }

    /**
     * Add an item to the list.
     */
    @ApiEndpoint(POST = "/objects", desc = "Add an item to the list")
    public void addObjects() {
        codexBasicService.addObject();
    }

    /**
     * @param id of item to be removed from the list
     */
    @ApiEndpoint(PUT = "/objects/{id}", desc = "Modify an item from the list")
    public void modify(@PathVariable("id") int id) {
        codexBasicService.modifyObject(id);
    }

    /**
     * @param id of item to be removed from the list
     */
    @ApiEndpoint(DELETE = "/objects/{id}", desc = "remove an item from the list",
            params = @ApiEndpoint.Param(name = "id", desc = "The object id to remove"))
    public void remove(@PathVariable("id") int id) {
        codexBasicService.removeObject(id);
    }

    /**
     * @param numOfItems number of items to add in list of objects
     * @return FutureWork
     */
    @ApiEndpoint(POST = "/objects/additional-data/{numOfItems}", desc = "Return the Future Work of Additional Data")
    public FutureWork additionalData(@PathVariable("numOfItems") int numOfItems) {
        FutureWork futureWork = codexBasicService.getAdditionalData(numOfItems);
        return futureWork;
    }

}
