package com.unibet.worktest.bank.service.impl;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.unibet.worktest.bank.service.LockManager;

@Service
public class LockManagerImpl<T> implements LockManager<T> {
	private static final Logger LOG = LoggerFactory.getLogger(LockManagerImpl.class);

	private ConcurrentHashMap<T, ReentrantLock> locks;

	public LockManagerImpl() {
		locks = new ConcurrentHashMap<>();
	}

	@Override
	public void acquireLocks(Collection<T> references) {
		for (T reference : references) {
			locks.putIfAbsent(reference, new ReentrantLock());
			locks.get(reference).lock();
			
			LOG.info("Lock acquired for reference {}", reference);
		}
	}

	@Override
	public void releaseLocks(Collection<T> references) {
		for (T reference : references) {
			locks.putIfAbsent(reference, new ReentrantLock());
			ReentrantLock lock = locks.get(reference);

			if (lock.isLocked() && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
			
			LOG.info("Lock released for reference {}", reference);
		}
	}

}
