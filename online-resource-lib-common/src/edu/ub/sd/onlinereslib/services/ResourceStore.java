package edu.ub.sd.onlinereslib.services;

import edu.ub.sd.onlinereslib.beans.Resource;
import edu.ub.sd.onlinereslib.beans.ResourceList;
import edu.ub.sd.onlinereslib.beans.User;
import edu.ub.sd.onlinereslib.services.exceptions.*;
import edu.ub.sd.onlinereslib.webframework.ServiceController;
import edu.ub.sd.onlinereslib.webframework.annotations.RequireService;
import edu.ub.sd.onlinereslib.webframework.annotations.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResourceStore extends ServiceController {

    private Store videos;
    private Store books;
    private Store music;
    private Store all;

    private Store apiVideos;
    private Store apiBooks;
    private Store apiMusic;

    private Map<String, Store> formatMap;
    private Map<String, Store> apiMap;

    private UserResourceListStore shoppingBaskets;
    private UserResourceListStore boughtProducts;

    @RequireService
    public UserStore userStore;

    @RequireService
    public GsonSerializer gson;

    @Override
    public void initialize() {
        log("Initializing resource store...");

        initializeMaps();

        try {
            importResources();
            importUserResources();
        } catch ( Exception ex ) {
            log(ex.getMessage(), ex);
        }

        log(String.format("Total files imported [%s]", all.size()));
    }

    @Override
    public void shutdown() {
        log("Resource store shutting down");

        try {
            persistUserResources();
        } catch ( Exception ex ) {
            log(ex.getMessage(), ex);
        }
    }

    public Resource getResource(String id) throws ResourceNotFoundException {
        if ( !all.containsKey(id) )
            throw new ResourceNotFoundException(id);

        return all.get(id);
    }

    public Resource getUserBoughtResource(HttpServletRequest request, String id) throws Exception {
        User user = userStore.getUser(request);
        Resource resource = getResource(id);
        ResourceList boughtResources = boughtProducts.get(user);
        if ( !boughtResources.contains(resource) )
            throw new NotBoughtResourceException();
        return resource;
    }

    public Collection<Resource> getAllResources() {
        return all.values();
    }

    public Collection<Resource> getBookResources() { return books.values(); }

    public Collection<Resource> getMusicResources() { return music.values(); }

    public Collection<Resource> getVideosResources() { return videos.values(); }

    public ResourceList getUserShoppingBasket(HttpServletRequest request) {
        User user = userStore.getUser(request);
        return shoppingBaskets.get(user);
    }

    public ResourceList getUserBoughtResources(HttpServletRequest request) {
        User user = userStore.getUser(request);
        return boughtProducts.get(user);
    }

    public Resource addResourceToShoppingBasket(HttpServletRequest request, String id) throws ResourceNotFoundException, AlreadyInShoppingBasketException, AlreadyBoughtResourceException {
        User user = getUser(request);
        Resource resourceToBuy = getResource(id);
        ResourceList shoppingBasket = shoppingBaskets.get(user);
        ResourceList boughtProduct = boughtProducts.get(user);

        if ( shoppingBasket.contains(resourceToBuy) ) {
            throw new AlreadyInShoppingBasketException(id);
        }

        if ( boughtProduct.contains(resourceToBuy) ) {
            throw new AlreadyBoughtResourceException(id);
        }

        log(String.format("User [%s] has added [%s] into shopping basket. User shopping basket size [%s]", user, resourceToBuy, shoppingBasket.size()));
        shoppingBasket.add(resourceToBuy);

        return resourceToBuy;
    }

    public Resource removeResourceFromShoppingBasket(HttpServletRequest request, String id)  throws ResourceNotFoundException {
        User user = getUser(request);
        Resource resourceToRemove = getResource(id);
        ResourceList shoppingBasket = shoppingBaskets.get(user);

        if ( !shoppingBasket.contains(resourceToRemove) ) {
            throw new ResourceNotFoundException(id);
        }

        log(String.format("User [%s] has removed [%s] from shopping basket. User shopping basket size [%s]", user, resourceToRemove, shoppingBasket.size()));
        shoppingBasket.remove(resourceToRemove);

        return resourceToRemove;
    }

    public ResourceList buyResourcesFromShoppingList(HttpServletRequest request) throws NotEnoughTokensException {
        User user = getUser(request);
        ResourceList shoppingBasket = shoppingBaskets.get(user);
        ResourceList boughtResources = boughtProducts.get(user);

        ResourceList newBoughtResources = new ResourceList();
        newBoughtResources.addAll(shoppingBasket);

        shoppingBasket.removeAll(boughtResources);
        userStore.removeTokens(user, shoppingBasket.getTotalCost());
        boughtResources.addAll(shoppingBasket);
        shoppingBasket.clear();

        return newBoughtResources;
    }

    public Resource buyResource(HttpServletRequest request, String id) throws ResourceNotFoundException, AlreadyBoughtResourceException, NotEnoughTokensException {
        User user = getUser(request);
        Resource resourceToBuy = getResource(id);
        ResourceList shoppingBasket = shoppingBaskets.get(user);
        ResourceList boughtProduct = boughtProducts.get(user);

        if ( boughtProduct.contains(resourceToBuy) ) {
            throw new AlreadyBoughtResourceException(id);
        }

        if ( shoppingBasket.contains(resourceToBuy) ) {
            shoppingBasket.remove(resourceToBuy);
        }

        userStore.removeTokens(user, resourceToBuy.getCost());
        boughtProduct.add(resourceToBuy);

        return resourceToBuy;
    }

    private User getUser(HttpServletRequest request) {
        User user = userStore.getUser(request);

        if ( user.isNew() ) {
            shoppingBaskets.put(user, new ResourceList());
            boughtProducts.put(user, new ResourceList());

            user.setIsNew(false);
        }

        return user;
    }

    public boolean isResourceBought(HttpServletRequest request, String id) throws Exception {
        User user = userStore.getUser(request);
        Resource resource = getResource(id);
        ResourceList boughtResources = boughtProducts.get(user);
        return boughtResources.contains(resource);
    }

    public Resource getResourceByTypeAndId(String type, String id) throws Exception {
        if ( apiMap.containsKey(type) ) {
            Store store = apiMap.get(type);
            if ( store.containsKey(id) ) {
                return store.get(id);
            }

            throw new ResourceNotFoundException(id);
        }

        throw new UnknownResourceTypeException();
    }

    public Collection<Resource> getResourcesByType(String type) throws Exception {
        if ( apiMap.containsKey(type) ) {
            return apiMap.get(type).values();
        }

        throw new UnknownResourceTypeException();
    }

    private void initializeMaps() {
        videos = new Store("video/webm");
        books = new Store("application/pdf");
        music = new Store("audio/mpeg");
        all = new Store("application/octet-stream");

        apiVideos = new Store("video/webm");
        apiBooks = new Store("application/pdf");
        apiMusic = new Store("audio/mpeg");

        formatMap = new HashMap<>();
        formatMap.put("webm", videos);
        formatMap.put("pdf", books);
        formatMap.put("mp3", music);

        formatMap.put("api/webm", apiVideos);
        formatMap.put("api/pdf", apiBooks);
        formatMap.put("api/mp3", apiMusic);

        apiMap = new HashMap<>();
        apiMap.put("AUDIO", apiMusic);
        apiMap.put("VIDEO", apiVideos);
        apiMap.put("BOOK", apiBooks);
    }

    private void importResources() throws Exception {
        //URL url = getServletContext().getResource("/WEB-INF/resources");
        String realPath = getServletContext().getRealPath("/WEB-INF/resources");
        //log(String.format("url path [%s]", url.getPath()));
        //log(String.format("url file [%s]", url.getFile()));
        //log(String.format("realPath [%s]", realPath));

        File resources = new File(realPath);
        File[] resourceList = resources.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory())
                    return false;
                else
                    return formatMap.containsKey(getFileExtension(pathname));
            }
        });

        if ( resourceList != null ) {
            for (File resource : resourceList) {
                importResource(resource);
            }
        }
    }

    private void importUserResources() throws Exception {
        boughtProducts = new UserResourceListStore(gson, "bought_products");
        shoppingBaskets = new UserResourceListStore(gson, "shopping_basket");

        Collection<User> users = userStore.getUserList();

        for ( User user : users ) {
            boughtProducts.load(user);
            shoppingBaskets.load(user);
        }
    }

    private void persistUserResources() throws Exception {
        log("Saving user bought products");
        boughtProducts.save();

        log("Saving user shopping baskets");
        shoppingBaskets.save();
    }

    private void importResource(File file) throws Exception {
        String ext = getFileExtension(file);
        Store store = formatMap.get(ext);
        Store apiStore = formatMap.get("api/" + ext);

        if ( store == null )
            throw new Exception("File type not supported");

        String propertiesPath = file.getAbsolutePath().replace(ext, "properties");
        File propertiesFile = new File(propertiesPath);

        if ( !propertiesFile.exists() )
            throw new Exception("Properties file does not exist");

        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFile));

        Resource resource = new Resource();
        resource.setName(properties.getProperty("name"));
        resource.setId(properties.getProperty("id"));
        resource.setCost(Integer.parseInt(properties.getProperty("cost")));
        resource.setExtension(ext);
        resource.setMimeType(store.getMimeType());
        resource.setFile(file);

        store.put(resource.getId(), resource);
        apiStore.put(resource.getName(), resource);
        all.put(resource.getId(), resource);

        log(String.format("Imported new resource %s", resource));
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int n = name.lastIndexOf('.') + 1;
        if ( n != -1 ) {
            return name.substring(n);
        } else {
            return null;
        }
    }

    class Store extends ConcurrentHashMap<String, Resource> {

        private String mimeType;

        public Store(String mimeType) {
            super();

            this.mimeType = mimeType;
        }

        public String getMimeType() {
            return mimeType;
        }

    }

    class UserResourceListStore extends ConcurrentHashMap<User, ResourceList> {

        private GsonSerializer gson;
        private String storeName;

        public UserResourceListStore(GsonSerializer gson, String storeName) {
            this.gson = gson;
            this.storeName = storeName;
        }

        public void load(User user) throws Exception {
            File userFile = getPersistenceFile(user);
            if ( !userFile.exists() ) {
                File userFileFolder = new File(getUserPath(user));
                userFileFolder.mkdirs();
                userFile.createNewFile();
                Files.write(userFile.toPath(), "[]".getBytes());
            }

            String[] resourceIds = gson.getStringListFromFile(getPersistenceFile(user));

            ResourceList resourceList = new ResourceList();
            for ( String resourceId : resourceIds ) {
                resourceList.add(all.get(resourceId));
            }

            put(user, resourceList);
        }

        public void save() throws Exception {
            for ( Entry<User, ResourceList> entry : this.entrySet() ) {
                User user = entry.getKey();
                ResourceList resourceList = entry.getValue();

                File userFile = getPersistenceFile(user);
                if ( !userFile.exists() ) {
                    File userFileFolder = new File(getUserPath(user));
                    userFileFolder.mkdirs();
                    userFile.createNewFile();
                }

                FileWriter writer = new FileWriter(getPersistenceFile(user));
                writer.write(gson.serialize(resourceList.getResourceIds()));
                writer.close();
            }
        }

        private File getPersistenceFile(User user) {
            String userPath = getUserPath(user);
            return new File(String.format("%s/%s.json", userPath, storeName));
        }

        private String getUserPath(User user) {
            String path = String.format("/WEB-INF/users_data/%s", user.getTomcatUsername());
            String realPath = getServletContext().getRealPath(path);

            if ( realPath == null ) {
                (new File(getServletContext().getRealPath("/WEB-INF/users_data/") + user.getTomcatUsername())).mkdirs();
                realPath = getServletContext().getRealPath(path);
            }

            return realPath;
        }

    }

}
