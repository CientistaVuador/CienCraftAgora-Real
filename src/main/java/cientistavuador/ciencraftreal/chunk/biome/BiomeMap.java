/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */
package cientistavuador.ciencraftreal.chunk.biome;

import cientistavuador.ciencraftreal.chunk.biome.definitionmap.BiomeDefinitionMaps;

/**
 *
 * @author Cien
 */
public class BiomeMap {
    
    public static final int MAP_SIZE = 64;
    public static final float TEMPERATURE_MAX = 50f;
    public static final float TEMPERATURE_MIN = -20f;
    public static final float HUMIDITY_MAX = 100f;
    public static final float HUMIDITY_MIN = 0f;
    
    private final BiomeRegister register;
    private final byte[] biomeMap = new byte[MAP_SIZE * MAP_SIZE];
    private BiomeDefinitionMaps maps = null;
    
    public BiomeMap(BiomeRegister register) {
        this.register = register;
    }

    public BiomeRegister getRegister() {
        return register;
    }
    
    public void createDefinitionMaps() {
        this.maps = new BiomeDefinitionMaps(this);
    }
    
    public int length() {
        return this.biomeMap.length;
    }
    
    public Biome getBiomeAtIndex(int i) {
        return register.getBiome(this.biomeMap[i]);
    }
    
    public Biome getBiomeAt(float humidity, float temperature) {
        humidity = ((humidity - HUMIDITY_MIN) / (HUMIDITY_MAX - HUMIDITY_MIN)) * MAP_SIZE;
        temperature = ((temperature - TEMPERATURE_MIN) / (TEMPERATURE_MAX - TEMPERATURE_MIN)) * MAP_SIZE;
        return register.getBiome(this.biomeMap[(int) (Math.floor(humidity) + (Math.floor(temperature) * MAP_SIZE))]);
    }
    
    public float getBiomeDefinitionAt(float humidity, float temperature, int field) {
        return this.maps.getFieldMap(field).valueAt(humidity, temperature);
    }
    
    public int fill(float humidityStart, float temperatureStart, float humidityEnd, float temperatureEnd, Biome biome) {
        humidityStart -= HUMIDITY_MIN;
        humidityEnd -= HUMIDITY_MIN;
        temperatureStart -= TEMPERATURE_MIN;
        temperatureEnd -= TEMPERATURE_MIN;
        
        float humidityLength = HUMIDITY_MAX - HUMIDITY_MIN;
        float temperatureLength = TEMPERATURE_MAX - TEMPERATURE_MIN;
        
        humidityStart /= humidityLength;
        humidityEnd /= humidityLength;
        temperatureStart /= temperatureLength;
        temperatureEnd /= temperatureLength;
        
        humidityStart *= MAP_SIZE;
        humidityEnd *= MAP_SIZE;
        temperatureStart *= MAP_SIZE;
        temperatureEnd *= MAP_SIZE;
        
        int xStart = (int) Math.floor(humidityStart);
        int xEnd = (int) Math.floor(humidityEnd);
        int yStart = (int) Math.floor(temperatureStart);
        int yEnd = (int) Math.floor(temperatureEnd);
        
        int filled = 0;
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                this.biomeMap[x + (y * MAP_SIZE)] = (byte) biome.getId();
                filled++;
            }
        }
        return filled;
    }
    
}
