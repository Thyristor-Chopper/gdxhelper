package io.potatogun.gdxhelper.entity.manager;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import io.potatogun.gdxhelper.entity.Entity;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 4, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u001aB1\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u001a\u0002\b\u000b¢\u0006\u0004\b\t\u0010\nJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005H\u0016J\t\u0010\u0011\u001a\u00020\u000fH\u0082\bJ\u001e\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\u0016H\u0016J\u0019\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0003H\u0082\bR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"},
   d2 = {"Lio/potatogun/gdxhelper/entity/manager/SweepPruneManager;", "Lio/potatogun/gdxhelper/entity/manager/LinearManager;", "capacity", "", "nearbyThreshold", "", "axis", "Lio/potatogun/gdxhelper/entity/manager/SweepPruneManager$Axis;", "slack", "<init>", "(IFLio/potatogun/gdxhelper/entity/manager/SweepPruneManager$Axis;F)V", "Lkotlin/jvm/JvmOverloads;", "keys", "Lcom/badlogic/gdx/utils/FloatArray;", "update", "", "delta", "sort", "getNearby", "entity", "Lio/potatogun/gdxhelper/entity/Entity;", "output", "Lcom/badlogic/gdx/utils/Array;", "lowerBound", "key", "size", "Axis", "endless-dead:gdxhelper"}
)
public final class SweepPruneManager extends LinearManager {
   @NotNull
   private final SweepPruneManager.Axis axis;
   private final float slack;
   @NotNull
   private final FloatArray keys;

   @JvmOverloads
   public SweepPruneManager(int capacity, float nearbyThreshold, @NotNull SweepPruneManager.Axis axis, float slack) {
      Intrinsics.checkNotNullParameter(axis, "axis");
      super(capacity, nearbyThreshold, true);
      this.axis = axis;
      this.slack = slack;
      this.keys = new FloatArray(capacity);
   }

   // $FF: synthetic method
   public SweepPruneManager(int var1, float var2, SweepPruneManager.Axis var3, float var4, int var5, DefaultConstructorMarker var6) {
      if ((var5 & 4) != 0) {
         var3 = SweepPruneManager.Axis.X;
      }

      if ((var5 & 8) != 0) {
         var4 = 0.0F;
      }

      this(var1, var2, var3, var4);
   }

   public void update(float delta) {
      super.update(delta);
      SweepPruneManager this_$iv = this;
      int $i$f$sort = false;
      int size$iv = this.allEntities.size;
      if (this.keys.size != size$iv) {
         this.keys.setSize(size$iv);
      }

      int i$iv;
      Entity entity$iv;
      int j$iv;
      boolean $i$f$getY;
      float y$iv;
      boolean $i$f$getY;
      Entity this_$iv$iv;
      boolean $i$f$getY;
      FloatArray var10000;
      if (this.axis == SweepPruneManager.Axis.Y) {
         for(i$iv = 1; i$iv < size$iv; ++i$iv) {
            entity$iv = (Entity)this_$iv.allEntities.get(i$iv);
            $i$f$getY = false;
            y$iv = entity$iv.position.getY();

            for(j$iv = i$iv - 1; j$iv >= 0; --j$iv) {
               this_$iv$iv = (Entity)this_$iv.allEntities.get(j$iv);
               $i$f$getY = false;
               if (!(this_$iv$iv.position.getY() > y$iv)) {
                  break;
               }

               this_$iv.allEntities.set(j$iv + 1, this_$iv.allEntities.get(j$iv));
            }

            this_$iv.allEntities.set(j$iv + 1, entity$iv);
         }

         for(i$iv = 0; i$iv < size$iv; ++i$iv) {
            var10000 = this_$iv.keys;
            entity$iv = (Entity)this_$iv.allEntities.get(i$iv);
            $i$f$getY = false;
            var10000.set(i$iv, entity$iv.position.getY());
         }
      } else {
         for(i$iv = 1; i$iv < size$iv; ++i$iv) {
            entity$iv = (Entity)this_$iv.allEntities.get(i$iv);
            $i$f$getY = false;
            y$iv = entity$iv.position.getX();

            for(j$iv = i$iv - 1; j$iv >= 0; --j$iv) {
               this_$iv$iv = (Entity)this_$iv.allEntities.get(j$iv);
               $i$f$getY = false;
               if (!(this_$iv$iv.position.getX() > y$iv)) {
                  break;
               }

               this_$iv.allEntities.set(j$iv + 1, this_$iv.allEntities.get(j$iv));
            }

            this_$iv.allEntities.set(j$iv + 1, entity$iv);
         }

         for(i$iv = 0; i$iv < size$iv; ++i$iv) {
            var10000 = this_$iv.keys;
            entity$iv = (Entity)this_$iv.allEntities.get(i$iv);
            $i$f$getY = false;
            var10000.set(i$iv, entity$iv.position.getX());
         }
      }

   }

   private final void sort() {
      int $i$f$sort = false;
      int size = this.allEntities.size;
      if (this.keys.size != size) {
         this.keys.setSize(size);
      }

      FloatArray var10000;
      int i;
      Entity entity;
      float y;
      int j;
      boolean $i$f$getY;
      boolean $i$f$getY;
      boolean $i$f$getY;
      Entity this_$iv;
      if (this.axis == SweepPruneManager.Axis.Y) {
         for(i = 1; i < size; ++i) {
            entity = (Entity)this.allEntities.get(i);
            $i$f$getY = false;
            y = entity.position.getY();

            for(j = i - 1; j >= 0; --j) {
               this_$iv = (Entity)this.allEntities.get(j);
               $i$f$getY = false;
               if (!(this_$iv.position.getY() > y)) {
                  break;
               }

               this.allEntities.set(j + 1, this.allEntities.get(j));
            }

            this.allEntities.set(j + 1, entity);
         }

         for(i = 0; i < size; ++i) {
            var10000 = this.keys;
            entity = (Entity)this.allEntities.get(i);
            $i$f$getY = false;
            var10000.set(i, entity.position.getY());
         }
      } else {
         for(i = 1; i < size; ++i) {
            entity = (Entity)this.allEntities.get(i);
            $i$f$getY = false;
            y = entity.position.getX();

            for(j = i - 1; j >= 0; --j) {
               this_$iv = (Entity)this.allEntities.get(j);
               $i$f$getY = false;
               if (!(this_$iv.position.getX() > y)) {
                  break;
               }

               this.allEntities.set(j + 1, this.allEntities.get(j));
            }

            this.allEntities.set(j + 1, entity);
         }

         for(i = 0; i < size; ++i) {
            var10000 = this.keys;
            entity = (Entity)this.allEntities.get(i);
            $i$f$getY = false;
            var10000.set(i, entity.position.getX());
         }
      }

   }

   public void getNearby(@NotNull Entity entity, @NotNull Array output) {
      Intrinsics.checkNotNullParameter(entity, "entity");
      Intrinsics.checkNotNullParameter(output, "output");
      output.clear();
      int $i$f$getX = false;
      float x = entity.position.getX();
      int $i$f$getY = false;
      float y = entity.position.getY();
      float range = this.nearbyThreshold + this.slack;
      float base = this.axis == SweepPruneManager.Axis.Y ? y : x;
      float maxKey = base + range;
      int size = this.allEntities.size;
      SweepPruneManager this_$iv = this;
      float n$iv = base - range;
      int $i$f$lowerBound = false;
      int lo$iv = 0;
      int hi$iv = size;

      while(lo$iv < hi$iv) {
         int mid$iv = lo$iv + hi$iv >>> 1;
         if (this_$iv.keys.get(mid$iv) < n$iv) {
            lo$iv = mid$iv + 1;
         } else {
            hi$iv = mid$iv;
         }
      }

      for(int i = lo$iv; i < size && this.keys.get(i) <= maxKey; ++i) {
         Entity e = (Entity)this.allEntities.get(i);
         if (e != entity) {
            int $i$f$abs = false;
            n$iv = e.position.getX() - x;
            $i$f$abs = false;
            if ((n$iv < 0.0F ? -n$iv : n$iv) <= this.nearbyThreshold) {
               $i$f$abs = false;
               n$iv = e.position.getY() - y;
               $i$f$abs = false;
               if ((n$iv < 0.0F ? -n$iv : n$iv) <= this.nearbyThreshold && e.distanceTo(entity) <= this.nearbyThreshold) {
                  output.add(e);
               }
            }
         }
      }

   }

   private final int lowerBound(float key, int size) {
      int $i$f$lowerBound = false;
      int lo = 0;
      int hi = size;

      while(lo < hi) {
         int mid = lo + hi >>> 1;
         if (this.keys.get(mid) < key) {
            lo = mid + 1;
         } else {
            hi = mid;
         }
      }

      return lo;
   }

   @JvmOverloads
   public SweepPruneManager(int capacity, float nearbyThreshold, @NotNull SweepPruneManager.Axis axis) {
      Intrinsics.checkNotNullParameter(axis, "axis");
      this(capacity, nearbyThreshold, axis, 0.0F, 8, (DefaultConstructorMarker)null);
   }

   @JvmOverloads
   public SweepPruneManager(int capacity, float nearbyThreshold) {
      this(capacity, nearbyThreshold, (SweepPruneManager.Axis)null, 0.0F, 12, (DefaultConstructorMarker)null);
   }

   @Metadata(
      mv = {2, 4, 0},
      k = 1,
      xi = 48,
      d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"},
      d2 = {"Lio/potatogun/gdxhelper/entity/manager/SweepPruneManager$Axis;", "", "<init>", "(Ljava/lang/String;I)V", "X", "Y", "endless-dead:gdxhelper"}
   )
   public static enum Axis {
      X,
      Y;

      // $FF: synthetic field
      private static final EnumEntries $ENTRIES = EnumEntriesKt.enumEntries((Enum[])$VALUES);

      @NotNull
      public static EnumEntries getEntries() {
         return $ENTRIES;
      }

      // $FF: synthetic method
      private static final SweepPruneManager.Axis[] $values() {
         SweepPruneManager.Axis[] var0 = new SweepPruneManager.Axis[]{X, Y};
         return var0;
      }
   }
}
