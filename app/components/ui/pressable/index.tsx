'use client';
import React from 'react';
import { createPressable } from '@gluestack-ui/core/pressable/creator';
import { Pressable as RNPressable } from 'react-native';
import type { GestureResponderEvent } from 'react-native';

import { tva } from '@gluestack-ui/utils/nativewind-utils';
import { withStyleContext } from '@gluestack-ui/utils/nativewind-utils';
import type { VariantProps } from '@gluestack-ui/utils/nativewind-utils';

const UIPressable = createPressable({
  Root: withStyleContext(RNPressable),
});

const pressableStyle = tva({
  base: 'data-[focus-visible=true]:outline-none data-[focus-visible=true]:ring-indicator-info data-[focus-visible=true]:ring-2 data-[disabled=true]:opacity-40',
});

const DOUBLE_PRESS_GUARD_MS = 300;

type IPressableProps = Omit<
  React.ComponentProps<typeof UIPressable>,
  'context'
> &
  VariantProps<typeof pressableStyle>;
const Pressable = React.forwardRef<
  React.ComponentRef<typeof UIPressable>,
  IPressableProps
>(function Pressable({ className, onPress, ...props }, ref) {
  const lastPressAtRef = React.useRef(0);

  const handlePress = onPress
    ? (event: GestureResponderEvent) => {
        const now = Date.now();
        if (now - lastPressAtRef.current < DOUBLE_PRESS_GUARD_MS) return;
        lastPressAtRef.current = now;
        onPress(event);
      }
    : undefined;

  return (
    <UIPressable
      {...props}
      onPress={handlePress}
      ref={ref}
      className={pressableStyle({
        class: className,
      })}
    />
  );
});

Pressable.displayName = 'Pressable';
export { Pressable };
