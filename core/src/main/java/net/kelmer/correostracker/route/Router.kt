package net.kelmer.correostracker.route

import android.os.Bundle
import androidx.annotation.IdRes

interface Router {

    @get:IdRes
    public val destinationId: Int

    public val arguments: Bundle
}
