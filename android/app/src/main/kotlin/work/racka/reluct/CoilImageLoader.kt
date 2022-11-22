package work.racka.reluct

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy

@Suppress("MagicNumber")
fun Context.customCoilImageLoader(): ImageLoader {
    return ImageLoader
        .Builder(this)
        .crossfade(true)
        .components {
            add(SvgDecoder.Factory())
        }
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder(this)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCachePolicy(CachePolicy.ENABLED)
        .diskCache {
            DiskCache.Builder()
                .directory(this.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.025)
                .build()
        }
        .build()
}
