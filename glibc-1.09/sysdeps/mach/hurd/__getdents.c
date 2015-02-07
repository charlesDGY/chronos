/* Copyright (C) 1992, 1993, 1994 Free Software Foundation, Ince	

This file is part of the GNU C Library.

The GNU C Library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public License as
published by the Free Software Foundation; either version 2 of the
License, or (at your option) any later version.

The GNU C Library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with the GNU C Library; see the file COPYING.LIB.  If
not, write to the Free Software Foundation, Inc., 675 Mass Ave,
Cambridge, MA 02139, USA.  */

#include <ansidecl.h>
#include <stddef.h>
#include <errno.h>
#include <sys/types.h>
#include <hurd.h>
#include <hurd/fd.h>

ssize_t
DEFUN(__getdirentries, (fd, buf, nbytes, basep),
      int fd AND PTR buf AND size_t nbytes AND off_t *basep)
{
  error_t err;
  int entriesread;
  char *data = buf;
  mach_msg_type_number_t bytesread = nbytes;

  /* Fault before taking any locks.  */
  *(volatile off_t *) basep = *basep;

  err = HURD_DPORT_USE (fd, __dir_readdir (port, &data, &bytesread,
					   *basep, -1, nbytes, &entriesread));
  if (err)
    return __hurd_dfail (fd, err);

  if (data != buf)
    {
      size_t copy = bytesread;
      if (copy > nbytes)
	/* The server has a violated the dir_readdir protocol.  */
	copy = nbytes;
      memcpy (buf, data, copy);
      __vm_deallocate (__mach_task_self (), (vm_address_t) data, bytesread);
      bytesread = copy;
    }

  *basep += entriesread;

  return bytesread;
}
