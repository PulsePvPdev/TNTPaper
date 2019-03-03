package kr.kieran.tntpaper.utilities.nbt;

import kr.kieran.tntpaper.TNTPaper;
import kr.kieran.tntpaper.utilities.nbt.utils.MinecraftVersion;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public class ItemNBTAPI {

    private TNTPaper plugin;
    private boolean compatible;
    private boolean jsonCompatible;
    private final String STRING_TEST_KEY;
    private final String INT_TEST_KEY;
    private final String DOUBLE_TEST_KEY;
    private final String BOOLEAN_TEST_KEY;
    private final String JSON_TEST_KEY;
    private final String COMP_TEST_KEY;
    private final String SHORT_TEST_KEY;
    private final String BYTE_TEST_KEY;
    private final String FLOAT_TEST_KEY;
    private final String LONG_TEST_KEY;
    private final String INTARRAY_TEST_KEY;
    private final String BYTEARRAY_TEST_KEY;
    private final String STRING_TEST_VALUE;
    private final int INT_TEST_VALUE;
    private final double DOUBLE_TEST_VALUE;
    private final boolean BOOLEAN_TEST_VALUE;
    private final short SHORT_TEST_VALUE;
    private final byte BYTE_TEST_VALUE;
    private final float FLOAT_TEST_VALUE;
    private final long LONG_TEST_VALUE;
    private final int[] INTARRAY_TEST_VALUE;
    private final byte[] BYTEARRAY_TEST_VALUE;

    public ItemNBTAPI(TNTPaper plugin) {
        this.plugin = plugin;
        compatible = true;
        jsonCompatible = true;
        STRING_TEST_KEY = "stringTest";
        INT_TEST_KEY = "intTest";
        DOUBLE_TEST_KEY = "doubleTest";
        BOOLEAN_TEST_KEY = "booleanTest";
        JSON_TEST_KEY = "jsonTest";
        COMP_TEST_KEY = "componentTest";
        SHORT_TEST_KEY = "shortTest";
        BYTE_TEST_KEY = "byteTest";
        FLOAT_TEST_KEY = "floatTest";
        LONG_TEST_KEY = "longTest";
        INTARRAY_TEST_KEY = "intarrayTest";
        BYTEARRAY_TEST_KEY = "bytearrayTest";
        STRING_TEST_VALUE = "TestString";
        INT_TEST_VALUE = 42;
        DOUBLE_TEST_VALUE = 1.5d;
        BOOLEAN_TEST_VALUE = true;
        SHORT_TEST_VALUE = 64;
        BYTE_TEST_VALUE = 7;
        FLOAT_TEST_VALUE = 13.37f;
        LONG_TEST_VALUE = (long) Integer.MAX_VALUE + 42L;
        INTARRAY_TEST_VALUE = new int[]{1337, 42, 69};
        BYTEARRAY_TEST_VALUE = new byte[]{8, 7, 3, 2};
        init();
    }

    private void init() {
        plugin.getLogger().log(Level.INFO, "Starting NBT checks, this may take a while.");
        MinecraftVersion.getVersion();
        MinecraftVersion.hasGsonSupport();
        plugin.getLogger().log(Level.INFO, "Classes: ");
        for (ClassWrapper clazz : ClassWrapper.values()) {
            if (clazz.getClazz() == null) {
                plugin.getLogger().log(Level.WARNING, clazz.name() + " did not find it's class.");
                compatible = false;
            }
        }
        for (ReflectionMethod method : ReflectionMethod.values()) {
            if (method.isCompatible() && !method.isLoaded()) {
                plugin.getLogger().log(Level.WARNING, method.name() + " did not find it's method.");
                compatible = false;
            }
        }
        try {
            ItemStack item = new ItemStack(Material.STONE, 1);
            NBTItem nbtItem = new NBTItem(item);

            nbtItem.setString(STRING_TEST_KEY, STRING_TEST_VALUE);
            nbtItem.setInteger(INT_TEST_KEY, INT_TEST_VALUE);
            nbtItem.setDouble(DOUBLE_TEST_KEY, DOUBLE_TEST_VALUE);
            nbtItem.setBoolean(BOOLEAN_TEST_KEY, BOOLEAN_TEST_VALUE);
            nbtItem.setByte(BYTE_TEST_KEY, BYTE_TEST_VALUE);
            nbtItem.setShort(SHORT_TEST_KEY, SHORT_TEST_VALUE);
            nbtItem.setLong(LONG_TEST_KEY, LONG_TEST_VALUE);
            nbtItem.setFloat(FLOAT_TEST_KEY, FLOAT_TEST_VALUE);
            nbtItem.setIntArray(INTARRAY_TEST_KEY, INTARRAY_TEST_VALUE);
            nbtItem.setByteArray(BYTEARRAY_TEST_KEY, BYTEARRAY_TEST_VALUE);
            nbtItem.addCompound(COMP_TEST_KEY);
            NBTCompound comp = nbtItem.getCompound(COMP_TEST_KEY);
            comp.setString(STRING_TEST_KEY, STRING_TEST_VALUE + "2");
            comp.setInteger(INT_TEST_KEY, INT_TEST_VALUE * 2);
            comp.setDouble(DOUBLE_TEST_KEY, DOUBLE_TEST_VALUE * 2);
            NBTList list = comp.getList("testlist", NBTType.NBTTagString);
            if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
                plugin.getLogger().log(Level.WARNING, "Skipped NBTList check due to unsupported 1.7 version. NBT may not work as expected.");
            } else {
                list.addString("test1");
                list.addString("test2");
                list.addString("test3");
                list.addString("test4");
                list.setString(2, "test42");
                list.remove(1);
            }
            NBTList taglist = comp.getList("complist", NBTType.NBTTagCompound);
            NBTListCompound lcomp = taglist.addCompound();
            lcomp.setDouble("double1", 0.3333);
            lcomp.setInteger("int1", 42);
            lcomp.setString("test1", "test1");
            lcomp.setString("test2", "test2");
            lcomp.remove("test1");

            item = nbtItem.getItem();
            nbtItem = new NBTItem(item);

            if (!nbtItem.hasKey(STRING_TEST_KEY)) {
                plugin.getLogger().log(Level.WARNING, "Failed to check a key. NBT may not work as expected.");
                compatible = false;
            }
            if (!(STRING_TEST_VALUE).equals(nbtItem.getString(STRING_TEST_KEY))
                    || nbtItem.getInteger(INT_TEST_KEY) != INT_TEST_VALUE
                    || nbtItem.getDouble(DOUBLE_TEST_KEY) != DOUBLE_TEST_VALUE
                    || nbtItem.getByte(BYTE_TEST_KEY) != BYTE_TEST_VALUE
                    || nbtItem.getShort(SHORT_TEST_KEY) != SHORT_TEST_VALUE
                    || nbtItem.getFloat(FLOAT_TEST_KEY) != FLOAT_TEST_VALUE
                    || nbtItem.getLong(LONG_TEST_KEY) != LONG_TEST_VALUE
                    || nbtItem.getIntArray(INTARRAY_TEST_KEY).length != (INTARRAY_TEST_VALUE).length
                    || nbtItem.getByteArray(BYTEARRAY_TEST_KEY).length != (BYTEARRAY_TEST_VALUE).length
                    || !nbtItem.getBoolean(BOOLEAN_TEST_KEY).equals(BOOLEAN_TEST_VALUE)) {
                plugin.getLogger().log(Level.WARNING, "One key does not equal the original value. NBT may not work as expected.");
                compatible = false;
            }
            nbtItem.setString(STRING_TEST_KEY, null);
            if (nbtItem.getKeys().size() != 10) {
                plugin.getLogger().log(Level.WARNING, "Wasn't able to remove a key (got " + nbtItem.getKeys().size() + " when expecting 10). NBT may not work as expected.");
                compatible = false;
            }
            comp = nbtItem.getCompound(COMP_TEST_KEY);
            if (comp == null) {
                plugin.getLogger().log(Level.WARNING, "Wasn't able to get the NBTCompound. NBT may not work as expected.");
                compatible = false;
            }
            if (!Objects.requireNonNull(comp).hasKey(STRING_TEST_KEY)) {
                plugin.getLogger().log(Level.WARNING, "Wasn't able to check a compound key. NBT may not work as expected.");
                compatible = false;
            }
            if (!(STRING_TEST_VALUE + "2").equals(comp.getString(STRING_TEST_KEY))
                    || comp.getInteger(INT_TEST_KEY) != INT_TEST_VALUE * 2
                    || comp.getDouble(DOUBLE_TEST_KEY) != DOUBLE_TEST_VALUE * 2
                    || comp.getBoolean(BOOLEAN_TEST_KEY) == BOOLEAN_TEST_VALUE) {
                plugin.getLogger().log(Level.WARNING, "One key does not equal the original compound value. NBT may not work as expected.");
                compatible = false;
            }

            list = comp.getList("testlist", NBTType.NBTTagString);
            if (comp.getType("testlist") != NBTType.NBTTagList) {
                plugin.getLogger().log(Level.WARNING, "Wasn't able to get the correct tag type. NBT may not work as expected.");
                compatible = false;
            }
            if (!list.getString(1).equals("test42") || list.size() != 3) {
                plugin.getLogger().log(Level.WARNING, "The list support got an error and may not work.");
            }
            taglist = comp.getList("complist", NBTType.NBTTagCompound);
            if (taglist.size() == 1) {
                lcomp = taglist.getCompound(0);
                if (lcomp.getKeys().size() != 3) {
                    plugin.getLogger().log(Level.WARNING, "Wrong key amount in tagList (" + lcomp.getKeys().size() + "). NBT may not work as expected.");
                    compatible = false;
                } else if (!(lcomp.getDouble("double1") == 0.3333 && lcomp.getInteger("int1") == 42
                        && lcomp.getString("test2").equals("test2") && !lcomp.hasKey("test1"))) {
                    plugin.getLogger().log(Level.WARNING, "One key in the tagList changed. NBT may not work as expected.");
                    compatible = false;
                }
            } else {
                plugin.getLogger().log(Level.WARNING, "tagList is empty. NBT may not work as expected.");
                compatible = false;
            }
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
            compatible = false;
        }
        testJson();
        try {
            NBTFile file = new NBTFile(new File(plugin.getDataFolder(), "test.nbt"));
            file.addCompound("testcomp").setString("test1", "ok");
            NBTCompound comp = file.getCompound("testcomp");
            comp.setString("test2", "ok");
            file.setLong("time", System.currentTimeMillis());
            file.setString("test", "test");
            file.save();
            file = new NBTFile(new File(plugin.getDataFolder(), "test.nbt"));
            System.out.println(file.asNBTString());
            file.getFile().delete();
            String str = file.asNBTString();
            NBTContainer rebuild = new NBTContainer(str);
            if (!str.equals(rebuild.asNBTString())) {
                plugin.getLogger().log(Level.WARNING, "Wasn't able to parse NBT from a string. NBT may not work as expected.");
                compatible = false;
            }
            ItemStack preitem = new ItemStack(Material.STICK, 5);
            ItemMeta premeta = preitem.getItemMeta();
            premeta.setDisplayName("test");
            preitem.setItemMeta(premeta);
            NBTContainer test1 = new NBTContainer();
            test1.setString("test1", "test");
            NBTContainer test2 = new NBTContainer();
            test2.setString("test2", "test");
            test2.addCompound("test").setLong("time", System.currentTimeMillis());
            test1.mergeCompound(test2);
            if (!test1.getString("test1").equals(test1.getString("test2"))) {
                plugin.getLogger().log(Level.WARNING, "Wasn't able to merge compounds. NBT may not work as expected.");
                compatible = false;
            }

            if (!plugin.getServer().getWorlds().isEmpty()) {
                World world = plugin.getServer().getWorlds().get(0);
                try {
                    if (!world.getEntitiesByClasses(Animals.class, Monster.class).isEmpty()) {
                        NBTEntity nbte = new NBTEntity(world.getEntitiesByClasses(Animals.class, Monster.class).iterator().next());
                        nbte.setString("INVALIDEKEY", "test");
                    }
                } catch (Exception ex) {
                    plugin.getLogger().log(Level.WARNING, "Wasn't able to use NBTEntities. NBT may not work as expected.");
                    compatible = false;
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
            compatible = false;
        }
        if (compatible) {
            if (jsonCompatible) {
                plugin.getLogger().info("NBT is compatible with the server.");
            } else {
                plugin.getLogger().log(Level.INFO, "NBT is mostly compatible with the server however, JSON serialization is not working properly. ");
            }
        } else {
            plugin.getLogger().log(Level.WARNING, "NBT does not work with your server version. Expect the unexpected.");
        }
    }

    private void testJson() {
        if (!MinecraftVersion.hasGsonSupport()) {
            plugin.getLogger().log(Level.WARNING, "Failed to find Gson. NBT may not work with JSON serialization/deserialization.");
            return;
        }
        try {
            ItemStack item = new ItemStack(Material.STONE, 1);
            NBTItem nbtItem = new NBTItem(item);

            nbtItem.setObject(JSON_TEST_KEY, new SimpleJsonTestObject());

            if (!nbtItem.hasKey(JSON_TEST_KEY)) {
                plugin.getLogger().log(Level.WARNING, "Failed to find JSON key. NBT may not work with JSON serialization/deserialization.");
                jsonCompatible = false;
            } else {
                SimpleJsonTestObject simpleObject = nbtItem.getObject(JSON_TEST_KEY, SimpleJsonTestObject.class);
                if (simpleObject == null) {
                    plugin.getLogger().log(Level.WARNING, "Failed to check JSON key. NBT may not work with JSON serialization/deserialization.");
                    jsonCompatible = false;
                } else if (!(STRING_TEST_VALUE).equals(simpleObject.getTestString())
                        || simpleObject.getTestInteger() != INT_TEST_VALUE
                        || simpleObject.getTestDouble() != DOUBLE_TEST_VALUE
                        || !simpleObject.isTestBoolean() == BOOLEAN_TEST_VALUE) {
                    plugin.getLogger().log(Level.WARNING, "One key does not match the original value (JSON). NBT may not work with JSON serialization/deserialization.");
                    jsonCompatible = false;
                }
            }
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
            plugin.getLogger().log(Level.WARNING, ex.getMessage());
            jsonCompatible = false;
        }
    }

    public NBTItem getNBTItem(ItemStack item) {
        return new NBTItem(item);
    }

    public class SimpleJsonTestObject {
        private String testString = STRING_TEST_VALUE;
        private int testInteger = INT_TEST_VALUE;
        private double testDouble = DOUBLE_TEST_VALUE;
        private boolean testBoolean = BOOLEAN_TEST_VALUE;

        public SimpleJsonTestObject() {
        }

        public String getTestString() {
            return testString;
        }

        public void setTestString(String testString) {
            this.testString = testString;
        }

        public int getTestInteger() {
            return testInteger;
        }

        public void setTestInteger(int testInteger) {
            this.testInteger = testInteger;
        }

        public double getTestDouble() {
            return testDouble;
        }

        public void setTestDouble(double testDouble) {
            this.testDouble = testDouble;
        }

        public boolean isTestBoolean() {
            return testBoolean;
        }

        public void setTestBoolean(boolean testBoolean) {
            this.testBoolean = testBoolean;
        }
    }


}
